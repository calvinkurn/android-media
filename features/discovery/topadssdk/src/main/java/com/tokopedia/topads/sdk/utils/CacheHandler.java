package com.tokopedia.topads.sdk.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * @author madi on 4/26/17.
 */

public class CacheHandler {
    public static final String KEY_PREFERRED_CATEGORY = "KEY_PREFERRED_CATEGORY";
    public static final String TOP_ADS_CACHE = "TOP_ADS_CACHE";

    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String SUFFIC_TOTAL = "_total";
    private static final String KEY_EXPIRE_TIME = "KEY_EXPIRE_TIME";

    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPrefs;

    public CacheHandler(Context context, String name) {
        sharedPrefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
    }

    public void putBoolean(String key, Boolean value) {
        editor.putBoolean(key, value);
    }

    public void putLong(String key, Long value) {
        editor.putLong(key, value);
    }

    public void putArrayListInteger(String key, ArrayList<Integer> value) {
        for (int i = 0; i < value.size(); i++) {
            editor.putInt(key + i, value.get(i));
        }
        editor.putInt(key + SUFFIC_TOTAL, value.size());
    }

    public void applyEditor() {
        editor.apply();
    }

    public String getString(String key) {
        return sharedPrefs.getString(key, null);
    }

    public String getString(String key, String defValue) {
        return sharedPrefs.getString(key, defValue);
    }

    public Long getLong(String key) {
        return sharedPrefs.getLong(key, 0);
    }

    public Integer getInt(String key) {
        return sharedPrefs.getInt(key, -1);
    }

    public Integer getInt(String key, int defVal) {
        return sharedPrefs.getInt(key, defVal);
    }

    public Boolean getBoolean(String key) {
        return sharedPrefs.getBoolean(key, false);
    }

    public Boolean getBoolean(String key, boolean defValue) {
        return sharedPrefs.getBoolean(key, defValue);
    }

    public ArrayList<Integer> getArrayListInteger(String key) {
        int total = sharedPrefs.getInt(key + SUFFIC_TOTAL, 0);
        ArrayList<Integer> value = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            value.add(getInt(key + i));
        }
        return value;
    }

    public void setExpire(int time) {
        putInt(KEY_EXPIRE_TIME, time);
        Long curr_time = System.currentTimeMillis() / 1000;
        putLong(KEY_TIMESTAMP, curr_time);
        applyEditor();
    }

    public Boolean isExpired() {
        int interval = getInt(KEY_EXPIRE_TIME);
        Long time = getLong(KEY_TIMESTAMP);
        Long curr_time = System.currentTimeMillis() / 1000;
        return (curr_time - time) > interval;
    }
    public static void clearCache(Context context, String name) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        sharedPrefs.edit().clear().apply();
    }
}
