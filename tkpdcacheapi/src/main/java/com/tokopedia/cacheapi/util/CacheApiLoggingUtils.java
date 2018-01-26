package com.tokopedia.cacheapi.util;

import android.util.Log;

public class CacheApiLoggingUtils {

    public static boolean logEnabled = false;

    public static void setLogEnabled(boolean logEnabled) {
        CacheApiLoggingUtils.logEnabled = logEnabled;
    }

    public static void dumper(String str) {
        if (logEnabled) {
            Log.i("CacheApi", str);
        }
    }
}