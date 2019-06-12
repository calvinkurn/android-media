package com.tokopedia.core.database.manager;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.cachemanager.PersistentCacheManager;

/**
 * Created by ricoharisin on 11/23/15.
 * Use PersistentCacheManager library direcly instead
 */
@Deprecated
public class GlobalCacheManager implements CacheManager {

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

    /**
     * @param duration value in second
     * @return
     */
    public GlobalCacheManager setCacheDuration(long duration) {
        Log.d(TAG, "Storing expired time: " + (System.currentTimeMillis()));
        this.expiredTime = System.currentTimeMillis() + (duration * 1000);
        return this;
    }

    public void store() {
        if (Key == null){
            return;
        }
        PersistentCacheManager.instance.put(Key, Value,
                this.expiredTime - System.currentTimeMillis());
    }

    @Override
    public void save(String key, String value, long durationInSeconds) {
        PersistentCacheManager.instance.put(key, value, durationInSeconds * 1000L);
    }

    public void delete(String key) {
        PersistentCacheManager.instance.delete(key);
    }

    @Override
    public String get(String key) {
        return PersistentCacheManager.instance.getString(key, null);
    }

    @Override
    public boolean isExpired(String key) {
        return PersistentCacheManager.instance.isExpired(key);
    }

    public String getValueString(String key) {
        return get(key);
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


    @Deprecated
    public void deleteAll() {
        PersistentCacheManager.instance.delete();
    }

    @Deprecated
    public static boolean isAvailable(String key) {
        return !PersistentCacheManager.instance.isExpired(key);
    }
}