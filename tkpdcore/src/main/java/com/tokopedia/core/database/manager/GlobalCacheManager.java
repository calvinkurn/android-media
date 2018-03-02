package com.tokopedia.core.database.manager;

import android.util.Log;

import com.google.gson.Gson;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.core.database.DbFlowDatabase;
import com.tokopedia.core.database.DbFlowOperation;
import com.tokopedia.core.database.model.SimpleDatabaseModel;
import com.tokopedia.core.database.model.SimpleDatabaseModel_Table;

import java.util.List;

/**
 * Created by ricoharisin on 11/23/15.
 */
public class GlobalCacheManager implements DbFlowOperation<SimpleDatabaseModel>,
        CacheManager {

    private String Key;
    private String Value;
    private long expiredTime = 0;
    private static String TAG = "CacheManager";

    public GlobalCacheManager() {

    }

    public GlobalCacheManager setKey(String key) {
        this.Key = key;
        return this;
    }

    public GlobalCacheManager setValue(String value) {
        this.Value = value;
        return this;
    }

    public GlobalCacheManager setCacheDuration(int duration) {
        Log.d(TAG, "Storing expired time: " + (System.currentTimeMillis()));
        this.expiredTime = System.currentTimeMillis() + (duration * 1000);
        return this;
    }

    public long getCacheDuration(int duration) {
        return System.currentTimeMillis() / 1000L + (duration * 1000);
    }

    public void store() {
        SimpleDatabaseModel simpleDB = new SimpleDatabaseModel();
        simpleDB.key = Key;
        simpleDB.value = Value;
        simpleDB.expiredTime = expiredTime;
        simpleDB.save();
    }

    @Override
    public void store(SimpleDatabaseModel data) {

    }

    @Override
    public void save(String key, String value, long durationInSeconds) {
        SimpleDatabaseModel simpleDB = new SimpleDatabaseModel();
        simpleDB.key = key;
        simpleDB.value = value;
        simpleDB.expiredTime = System.currentTimeMillis() + durationInSeconds * 1000L;
        simpleDB.save();
    }

    public void delete(String key) {
        new Delete().from(SimpleDatabaseModel.class).where(SimpleDatabaseModel_Table.key.is(key)).execute();
    }

    @Override
    public String get(String key) {
        SimpleDatabaseModel cache = new Select().from(SimpleDatabaseModel.class)
                .where(SimpleDatabaseModel_Table.key.is(key)).querySingle();
        if (cache == null)
            return null;
        if (isExpired(cache.expiredTime)) {
            return null;
        } else {
            return cache.value;
        }
    }

    @Override
    public boolean isExpired(String key) {
        SimpleDatabaseModel cache = new Select().from(SimpleDatabaseModel.class)
                .where(SimpleDatabaseModel_Table.key.is(key)).querySingle();
        return cache == null || isExpired(cache.expiredTime);
    }

    public void bulkInsert(List<String> key, List<String> value) {
        final DatabaseWrapper database = FlowManager.getDatabase(DbFlowDatabase.NAME).getWritableDatabase();
        database.beginTransaction();
        try {
            for (int i = 0; i < key.size(); i++) {
                SimpleDatabaseModel simpleDB = new SimpleDatabaseModel();
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
        SimpleDatabaseModel cache = new Select().from(SimpleDatabaseModel.class)
                .where(SimpleDatabaseModel_Table.key.is(key)).querySingle();
        if (cache == null)
            return null;
        if (isExpired(cache.expiredTime)) {
            throw new RuntimeException("Cache is expired!!");
        } else {
            return cache.value;
        }
    }

    @Override
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

    @Override
    public SimpleDatabaseModel getData(String key) {
        return null;
    }

    @Override
    public List<SimpleDatabaseModel> getDataList(String key) {
        return null;
    }

    public void deleteAll() {
        new Delete().from(SimpleDatabaseModel.class).execute();
    }

    public static boolean isAvailable(String key) {
        SimpleDatabaseModel cache = new Select().from(SimpleDatabaseModel.class)
                .where(SimpleDatabaseModel_Table.key.is(key)).querySingle();
        return cache != null;
    }
}