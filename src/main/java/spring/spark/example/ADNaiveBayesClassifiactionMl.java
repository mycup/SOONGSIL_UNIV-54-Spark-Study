package spring.spark.example;


import java.util.Arrays;
import java.util.List;

// $example off$
import org.apache.spark.SparkConf;
import org.apache.spark.annotation.DeveloperApi;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.IDF;
import org.apache.spark.ml.feature.IDFModel;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.ml.linalg.DenseVector;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.classification.NaiveBayes;
import org.apache.spark.ml.classification.NaiveBayesModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.Tuple2;
/**
 * ���̺� ���������� �̿��� ���� ī�װ��� �з�. 
 * @author skan
 *
 */
public class ADNaiveBayesClassifiactionMl {
	final static Logger logger = LoggerFactory.getLogger(ADNaiveBayesClassifiactionMl.class);
	
	public static void main(String[] args) throws Exception {
		
		logger.info("ADNaiveBayesClassifiaction Start ~ ~");
		
		System.setProperty("hadoop.home.dir", "D:/example/spark/spark-2.1.0-bin-hadoop2.7/spark-2.1.0-bin-hadoop2.7");
		
		SparkConf sparkConf = new SparkConf().setAppName("ADNaiveBayesClassifiaction").setMaster("local[*]");
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		SparkSession spark = SparkSession
			      .builder()
			      .appName("ADNaiveBayesClassifiaction")
			      .getOrCreate() ;
		
		
		// Input data: Each row is a bag of words from a sentence or document.
		List<Row> data = Arrays.asList(
		  RowFactory.create(1.0 , "�ڵ��� ������ ���� ���׿��극���� �����̵�"),
		  RowFactory.create(3.0 , "������̼� ���� GPS ���� ���߱��� �������"),
		  RowFactory.create(3.0 , "������̼� ���� GPS ���� ���߱��� �������"),
		  RowFactory.create(3.0 , "������̼� ���� GPS ���� ���߱��� �������"),
		  RowFactory.create(3.0 , "������̼� ���� GPS ���� ���߱��� �������"),
		  RowFactory.create(3.0 , "������̼� ���� GPS ���� ���߱��� �������"),
		  RowFactory.create(3.0 , "������̼� ���� GPS ���� ���߱��� �������"),
		  RowFactory.create(3.0 , "������̼� ���� GPS ���� ���߱��� �������"),
		  RowFactory.create(3.0 , "������̼� ���� GPS ���� ���߱��� �������"),
		  RowFactory.create(3.0 , "������̼� ���� GPS ���� ���߱��� �������"),
		  RowFactory.create(3.0 , "������̼� ���� GPS ���� ���߱��� �������"),
		  RowFactory.create(3.0 , "������̼� ���� GPS ���� ���߱��� �������"),
		  RowFactory.create(3.0 , "������̼� ���� GPS ���� ���߱��� �������"),
		  RowFactory.create(3.0 , "������̼� ���� GPS ���� ���߱��� �������"),
		  RowFactory.create(0.0 , "���� ����� �ƽþƳ� �����װ�"),
		  RowFactory.create(0.0 , "���� ����� �ƽþƳ� �����װ�"),
		  RowFactory.create(0.0 , "���� ����� �ƽþƳ� �����װ�"),
		  RowFactory.create(0.0 , "���� ����� �ƽþƳ� �����װ�"),
		  RowFactory.create(0.0 , "���� ����� �ƽþƳ� �����װ�"),
		  RowFactory.create(0.0 , "���� ����� �ƽþƳ� �����װ�"),
		  RowFactory.create(0.0 , "���� ����� �ƽþƳ� �����װ�"),
		  RowFactory.create(1.0 , "�ڵ��� ������ ���� ���׿��극���� �����̵�"),
		  RowFactory.create(1.0 , "�ڵ��� ���� ������ �ٵ� �����̵�"),
		  RowFactory.create(1.0 , "�ڵ��� ���� ������ �ٵ� �����̵�"),
		  RowFactory.create(1.0 , "�ڵ��� ���� ������ �ٵ� �����̵�"),
		  RowFactory.create(1.0 , "�ڵ��� ���� ������ �ٵ� �����̵�"),
		  RowFactory.create(1.0 , "�ڵ��� ���� ������ �ٵ� �����̵�"),
		  RowFactory.create(1.0 , "�ڵ��� ���� ������ �ٵ� �����̵�"),
		  RowFactory.create(1.0 , "�ڵ��� ���� ������ �ٵ� �����̵�"),
		  RowFactory.create(1.0 , "�ڵ��� ���� ������ �ٵ� �����̵�"),
		  RowFactory.create(1.0 , "�ڵ��� ���� ������ �ٵ� �����̵�"),
		  RowFactory.create(1.0 , "�ڵ��� ���� ������ �ٵ� �����̵�"),
		  RowFactory.create(1.0 , "�ڵ��� ���� ������ �ٵ� �����̵�"),
		  RowFactory.create(1.0 , "�ڵ��� ���� ������ �ٵ� �����̵�"),
		  RowFactory.create(1.0 , "�ڵ��� ���� ������ �ٵ� �����̵�"),
		  RowFactory.create(1.0 , "�ڵ��� ���� ������ �ٵ� �����̵�"),
		  RowFactory.create(0.0  , "Logistic regression models are neat"),
		  RowFactory.create(3.0, "I wish Java could use case classes"),
		  RowFactory.create(2.0, "��Ÿ�� ���̵� ����  ��Ƽ �÷��� ��� ���̵�"),
		  RowFactory.create(2.0, "��Ÿ�� ���̵� ����  ��Ƽ �÷��� ��� ���̵�"),
		  RowFactory.create(2.0, "��Ÿ�� ���̵� ���� ��Ƽ �÷��� ��� ���̵�"),
		  RowFactory.create(2.0, "��Ÿ�� ���̵� ����  ��Ƽ �÷��� ��� ���̵�"),
		  RowFactory.create(2.0, "��Ÿ�� ���̵� ����  ��Ƽ �÷��� ��� ���̵�"),
		  RowFactory.create(2.0, "��Ÿ�� ���̵� ����  ��Ƽ �÷��� ��� ���̵�"),
		  RowFactory.create(2.0, "��Ÿ�� ���̵� ����  ��Ƽ �÷��� ��� ���̵�"),
		  RowFactory.create(2.0, "��Ÿ�� ���̵� ����  ��Ƽ �÷��� ��� ���̵�"),
		  RowFactory.create(2.0, "��Ÿ�� ���̵� ����  ��Ƽ �÷��� ��� ���̵�"),
		  RowFactory.create(2.0, "��Ÿ�� ���̵� ����  ��Ƽ �÷��� ��� ���̵�"),
		  RowFactory.create(2.0, "��Ÿ�� ���̵� ����  ��Ƽ �÷��� ��� ���̵�"),
		  RowFactory.create(2.0, "��Ÿ�� ���̵� ����  ��Ƽ �÷��� ��� ���̵�"),
		  RowFactory.create(1.0, "��Ÿ�� ���̵� ����  ��Ƽ �÷��� ��� ���̵�")
		);
		StructType schema = new StructType(
				new StructField[] { 
						new StructField("label", DataTypes.DoubleType, false, Metadata.empty()),
						new StructField("sentence", DataTypes.StringType, false, Metadata.empty()) });
		Dataset<Row> sentenceData = spark.createDataFrame(data, schema);

		Tokenizer tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words");
		Dataset<Row> wordsData = tokenizer.transform(sentenceData);

		// Ű���� �ؽ� 
		int numFeatures = 10000;
		HashingTF hashingTF = new HashingTF()
									.setInputCol("words")
									.setOutputCol("rawFeatures")
									.setNumFeatures(numFeatures);
		Dataset<Row> featurizedData = hashingTF.transform(wordsData);
		logger.info("//////////////////////////////////////");
		logger.info("//			hashingTF DATA			  ");
		//featurizedData.show();
		
		// alternatively, CountVectorizer can also be used to get term frequency
		// vectors
		// ��� �׸񿡴��� ī���� ���� ���� 
		IDF idf = new IDF().setInputCol("rawFeatures").setOutputCol("features");
		IDFModel idfModel = idf.fit(featurizedData);

		// ��� �׸� ��� ���� ����ġ ����
		logger.info("//////////////////////////////////////");
		logger.info("//			TDIDF - Model DATA			  ");
		Dataset<Row> rescaledData = idfModel.transform(featurizedData);
		rescaledData.show(1000);
		//rescaledData.select("label","words" , "features", "rawFeatures").show();
		
		////////////////////////////////////////////////////////////////
		// RANDOM  Ʈ���ε����� 60% , �׽�Ʈ ������ 40%
		////////////////////////////////////////////////////////////////
		Dataset<Row>[] splits  = rescaledData.randomSplit(new double[] { 0.7, 0.3 });
		Dataset<Row> training = splits[0]; // training set
		Dataset<Row> test = splits[1]; // test set
		
		// create the trainer and set its parameters
		NaiveBayes nb = new NaiveBayes();

		// train the model
		NaiveBayesModel model = nb.fit(training);

		// Select example rows to display.
		Dataset<Row> predictions = model.transform(test);
		predictions.show();
		
		List<Row> listOne = predictions.collectAsList();
		listOne.forEach(prd->{
			logger.info("/////////////////////////////////////");
			
			double label  = prd.getDouble(0);
			String sentence = prd.getString(1);
			
			DenseVector probabilitys  = (DenseVector)prd.get(6);
			logger.info("�׽�Ʈ �� [{}] �ؽ�Ʈ =[{}]", label, sentence);
			Arrays.stream(probabilitys.values())
						.sorted()
						.forEach( probability->
							logger.info("�켱����  ���� Ȯ�� = {}" ,probability)
					);
		});
				

		// compute accuracy on the test set
		MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
															.setLabelCol("label")
															.setPredictionCol("prediction")
															.setMetricName("accuracy");
		
		double accuracy = evaluator.evaluate(predictions);
		System.out.println("Test set accuracy = " + accuracy);
		// $example off$
		
		jsc.stop();
	}
}

