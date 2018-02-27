package com.tokopedia.cacheapi.util;

import android.util.Log;

public class CacheApiLoggingUtils {

    private static final String TAG = "CacheApi";
    private static final int MAX_LOG_LENGTH = 4000;
    private static boolean logEnabled = false;

    public static void setLogEnabled(boolean logEnabled) {
        CacheApiLoggingUtils.logEnabled = logEnabled;
    }

    public static void dumper(String str) {
        if (!logEnabled) {
            return;
        }
        if (str.length() > MAX_LOG_LENGTH) {
            Log.v(TAG, str);
            return;
        }
        Log.v(TAG, "sb.length = " + str.length());
        int chunkCount = str.length() / MAX_LOG_LENGTH;     // integer division
        for (int i = 0; i <= chunkCount; i++) {
            int max = MAX_LOG_LENGTH * (i + 1);
            if (max >= str.length()) {
                Log.v(TAG, str.substring(MAX_LOG_LENGTH * i));
            } else {
                Log.v(TAG, str.substring(MAX_LOG_LENGTH * i, max));
            }
        }
    }
}