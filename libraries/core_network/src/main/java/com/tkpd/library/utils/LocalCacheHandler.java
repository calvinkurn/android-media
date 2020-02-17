package com.tkpd.library.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

@Deprecated
public class LocalCacheHandler {

    private Editor editor;
    private SharedPreferences sharedPrefs;

    public LocalCacheHandler(SharedPreferences sharedPreferences) {
        sharedPrefs = sharedPreferences;
        editor = sharedPrefs.edit();
    }

    public LocalCacheHandler(Context context, String name) {
        if (context != null && !TextUtils.isEmpty(name)) {
            sharedPrefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
            editor = sharedPrefs.edit();
        }
    }

    public static void clearCache(Context context, String name) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        sharedPrefs.edit().clear().apply();
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

    public Long getLong(String key, Long defVal) {
        return sharedPrefs.getLong(key, defVal);
    }

    public Long getLong(String key, int defVal) {
        return sharedPrefs.getLong(key, defVal);
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

    public void setExpire(int time) {
        putInt("expired_time", time);
        Long curr_time = System.currentTimeMillis() / 1000;
        putLong("timestamp", curr_time);
        applyEditor();
    }

    public Boolean isExpired() {
        int interval = getInt("expired_time");
        Long time = getLong("timestamp");
        Long curr_time = System.currentTimeMillis() / 1000;
        return (curr_time - time) > interval;
    }

    public void clearCache(String name) {
        editor.clear().apply();
    }

}