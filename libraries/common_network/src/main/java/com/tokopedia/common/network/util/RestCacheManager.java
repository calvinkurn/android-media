package com.tokopedia.common.network.util;

import android.util.Log;

import com.google.gson.Gson;
import com.tokopedia.common.network.data.db.RestDatabaseDao;
import com.tokopedia.common.network.data.db.RestDatabaseModel;

import java.util.ArrayList;
import java.util.List;

public class RestCacheManager {

    private static String TAG = "RestCacheManager";
    private RestDatabaseDao databaseDao;
    public RestCacheManager() {
        databaseDao =  NetworkClient.getRestDatabase().getRestDatabaseDao();
    }

    public void save(String key, String value, long durationInMilliSeconds) {
        RestDatabaseModel simpleDB = new RestDatabaseModel();
        simpleDB.key = key;
        simpleDB.value = value;
        simpleDB.expiredTime = System.currentTimeMillis() + durationInMilliSeconds;
        databaseDao.insertSingle(simpleDB);
    }

    public void delete(String key) {
        RestDatabaseModel simpleDB = new RestDatabaseModel();
        simpleDB.key = key;
        databaseDao.delete(simpleDB);
    }

    public String get(String key) {
        RestDatabaseModel cache = databaseDao.getRestModel(key);
        if (cache == null)
            return null;
        if (isExpired(cache.expiredTime)) {
            return null;
        } else {
            return cache.value;
        }
    }

    public boolean isExpired(String key) {
        RestDatabaseModel cache = databaseDao.getRestModel(key);
        return cache == null || isExpired(cache.expiredTime);
    }

    public void bulkInsert(List<String> key, List<String> value) {
        List<RestDatabaseModel> models = new ArrayList<>();
        try {
            for (int i = 0; i < key.size(); i++) {
                RestDatabaseModel simpleDB = new RestDatabaseModel();
                simpleDB.key = key.get(i);
                simpleDB.value = value.get(i);
                models.add(simpleDB);
            }
            databaseDao.insertMultiple(models);
        } catch (Exception e){e.getLocalizedMessage();}
    }

    public String getValueString(String key) {
        RestDatabaseModel cache = databaseDao.getRestModel(key);
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

    public RestDatabaseModel getData(String key) {
        return null;
    }

    public List<RestDatabaseModel> getDataList(String key) {
        return null;
    }

    public void deleteAll() {
        databaseDao.deleteTable();
    }

    public static boolean isAvailable(String key) {
        RestDatabaseModel cache = NetworkClient.getRestDatabase().getRestDatabaseDao().getRestModel(key);
        return cache != null;
    }
}
