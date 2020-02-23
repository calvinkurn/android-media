package com.tokopedia.graphql;

import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.data.db.GraphqlDatabaseDao;
import com.tokopedia.graphql.data.db.GraphqlDatabaseModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GraphqlCacheManager {

    private String Key;
    private String Value;
    private long expiredTime = 0;
    private GraphqlDatabaseDao databaseDao;

    public static long lastDeleteExpired = 0L;
    public final static long periodOfExpirationDeletion = TimeUnit.MINUTES.toMillis(3);

    public GraphqlCacheManager() {
        databaseDao = GraphqlClient.getGraphqlDatabase().getGraphqlDatabaseDao();
    }

    public GraphqlCacheManager setKey(String key) {
        this.Key = key;
        return this;
    }

    public GraphqlCacheManager setValue(String value) {
        this.Value = value;
        return this;
    }

    /**
     * @param duration value in second
     * @return
     */
    public GraphqlCacheManager setCacheDuration(long duration) {
        this.expiredTime = System.currentTimeMillis() + (duration * 1000);
        return this;
    }

    public long getCacheDuration(int duration) {
        return System.currentTimeMillis() / 1000L + (duration * 1000);
    }

    public void store() {
        GraphqlDatabaseModel simpleDB = new GraphqlDatabaseModel();
        simpleDB.key = Key;
        simpleDB.value = Value;
        simpleDB.expiredTime = expiredTime;
        databaseDao.insertSingle(simpleDB);
    }

    public void store(GraphqlDatabaseModel data) {

    }

    public void save(String key, String value, long durationInMiliSeconds) {
        GraphqlDatabaseModel simpleDB = new GraphqlDatabaseModel();
        simpleDB.key = key;
        simpleDB.value = value;
        simpleDB.expiredTime = System.currentTimeMillis() + durationInMiliSeconds;
        databaseDao.insertSingle(simpleDB);
    }

    public void delete(String key) {
        GraphqlDatabaseModel simpleDB = new GraphqlDatabaseModel();
        simpleDB.key = key;
        databaseDao.delete(simpleDB);
    }

    public String get(String key) {
        long currentTime = System.currentTimeMillis();
        GraphqlDatabaseModel cache = databaseDao.getGraphqlModel(key, currentTime);
        deleteExpiredRecord(currentTime);
        if (cache == null) {
            return null;
        } else {
            return cache.value;
        }
    }

    public void deleteExpiredRecord(Long currentTime){
        try {
            if (currentTime - lastDeleteExpired > periodOfExpirationDeletion) {
                databaseDao.deleteExpiredRows(currentTime);
                lastDeleteExpired = currentTime;
            }
        }catch (Exception ignored) { }
    }

    public boolean isExpired(String key) {
        GraphqlDatabaseModel cache = databaseDao.getGraphqlModel(key);
        return cache == null || isExpired(cache.expiredTime);
    }

    public void bulkInsert(List<String> key, List<String> value) {
        List<GraphqlDatabaseModel> models = new ArrayList<>();
        try {
            for (int i = 0; i < key.size(); i++) {
                GraphqlDatabaseModel simpleDB = new GraphqlDatabaseModel();
                simpleDB.key = key.get(i);
                simpleDB.value = value.get(i);
                models.add(simpleDB);
            }
            databaseDao.insertMultiple(models);
        } catch (Exception e){e.getLocalizedMessage();}
    }

    public boolean isExpired(long expiredTime) {
        if (expiredTime == 0) return false;
        return expiredTime < System.currentTimeMillis();
    }

    public GraphqlDatabaseModel getData(String key) {
        return null;
    }

    public List<GraphqlDatabaseModel> getDataList(String key) {
        return null;
    }

    public void deleteAll() {
        databaseDao.deleteTable();
    }

    public static boolean isAvailable(String key) {
        GraphqlDatabaseModel cache = GraphqlClient.getGraphqlDatabase().getGraphqlDatabaseDao().getGraphqlModel(key);
        return cache != null;
    }
}
