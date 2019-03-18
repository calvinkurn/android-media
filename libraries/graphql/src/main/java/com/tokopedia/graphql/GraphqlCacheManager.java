package com.tokopedia.graphql;

import android.util.Log;

import com.google.gson.Gson;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.data.db.GraphqlDatabase;
import com.tokopedia.graphql.data.db.GraphqlDatabaseDao;
import com.tokopedia.graphql.data.db.GraphqlDatabaseModel;

import java.util.ArrayList;
import java.util.List;

public class GraphqlCacheManager {

    private String Key;
    private String Value;
    private long expiredTime = 0;
    private static String TAG = "GraphqlCacheManager";
    private GraphqlDatabaseDao databaseDao;

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
        Log.d(TAG, "Storing expired time: " + (System.currentTimeMillis()));
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

    public void save(String key, String value, long durationInMilliSeconds) {
        GraphqlDatabaseModel simpleDB = new GraphqlDatabaseModel();
        simpleDB.key = key;
        simpleDB.value = value;
        simpleDB.expiredTime = System.currentTimeMillis() + durationInMilliSeconds;
        databaseDao.insertSingle(simpleDB);
    }

    public void delete(String key) {
        GraphqlDatabaseModel simpleDB = new GraphqlDatabaseModel();
        simpleDB.key = key;
        databaseDao.delete(simpleDB);
    }

    public String get(String key) {
        GraphqlDatabaseModel cache = databaseDao.getGraphqlModel(key);
        if (cache == null)
            return null;
        if (isExpired(cache.expiredTime)) {
            return null;
        } else {
            return cache.value;
        }
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

    public String getValueString(String key) {
        GraphqlDatabaseModel cache = databaseDao.getGraphqlModel(key);
        if (cache == null)
            return null;
        if (isExpired(cache.expiredTime)) {
            throw new RuntimeException("Cache is expired!!");
        } else {
            return cache.value;
        }
    }

    public <T> T getConvertObjData(String key, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(getValueString(key), clazz);
    }

    public boolean isExpired(long expiredTime) {
        if (expiredTime == 0) return false;
        Log.i(TAG, "Cache expired time: " + expiredTime);
        Log.i(TAG, "Cache current time: " + System.currentTimeMillis());
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
