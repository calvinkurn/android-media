package com.tokopedia.networklib.util;

import android.util.Log;

import com.google.gson.Gson;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.tokopedia.networklib.data.model.DbMetadata;
import com.tokopedia.networklib.data.model.RestDatabaseModel;
import com.tokopedia.networklib.data.model.RestDatabaseModel_Table;

import java.util.List;

public class RestCacheManager {

    private String Key;
    private String Value;
    private long expiredTime = 0;
    private static String TAG = "RestCacheManager";

    public RestCacheManager() {

    }

    public RestCacheManager setKey(String key) {
        this.Key = key;
        return this;
    }

    public RestCacheManager setValue(String value) {
        this.Value = value;
        return this;
    }

    /**
     * @param duration value in second
     * @return
     */
    public RestCacheManager setCacheDuration(long duration) {
        Log.d(TAG, "Storing expired time: " + (System.currentTimeMillis()));
        this.expiredTime = System.currentTimeMillis() + (duration * 1000);
        return this;
    }

    public long getCacheDuration(int duration) {
        return System.currentTimeMillis() / 1000L + (duration * 1000);
    }

    public void store() {
        RestDatabaseModel simpleDB = new RestDatabaseModel();
        simpleDB.key = Key;
        simpleDB.value = Value;
        simpleDB.expiredTime = expiredTime;
        simpleDB.save();
    }

    public void store(RestDatabaseModel data) {

    }

    public void save(String key, String value, long durationInSeconds) {
        RestDatabaseModel simpleDB = new RestDatabaseModel();
        simpleDB.key = key;
        simpleDB.value = value;
        simpleDB.expiredTime = System.currentTimeMillis() + durationInSeconds * 1000L;
        simpleDB.save();
    }

    public void delete(String key) {
        new Delete().from(RestDatabaseModel.class).where(RestDatabaseModel_Table.key.is(key)).execute();
    }

    public String get(String key) {
        RestDatabaseModel cache = new Select().from(RestDatabaseModel.class)
                .where(RestDatabaseModel_Table.key.is(key)).querySingle();
        if (cache == null)
            return null;
        if (isExpired(cache.expiredTime)) {
            return null;
        } else {
            return cache.value;
        }
    }

    public boolean isExpired(String key) {
        RestDatabaseModel cache = new Select().from(RestDatabaseModel.class)
                .where(RestDatabaseModel_Table.key.is(key)).querySingle();
        return cache == null || isExpired(cache.expiredTime);
    }

    public void bulkInsert(List<String> key, List<String> value) {
        final DatabaseWrapper database = FlowManager.getDatabase(DbMetadata.NAME).getWritableDatabase();
        database.beginTransaction();
        try {
            for (int i = 0; i < key.size(); i++) {
                RestDatabaseModel simpleDB = new RestDatabaseModel();
                simpleDB.key = key.get(i);
                simpleDB.value = value.get(i);
                simpleDB.save();
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public String getValueString(String key) {
        RestDatabaseModel cache = new Select().from(RestDatabaseModel.class)
                .where(RestDatabaseModel_Table.key.is(key)).querySingle();
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
        new Delete().from(RestDatabaseModel.class).execute();
    }

    public static boolean isAvailable(String key) {
        RestDatabaseModel cache = new Select().from(RestDatabaseModel.class)
                .where(RestDatabaseModel_Table.key.is(key)).querySingle();
        return cache != null;
    }
}
