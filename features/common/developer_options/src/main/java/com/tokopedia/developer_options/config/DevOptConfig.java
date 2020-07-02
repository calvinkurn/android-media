package com.tokopedia.developer_options.config;

import android.content.Context;
import android.content.SharedPreferences;

public class DevOptConfig {

    public static final String CHUCK_ENABLED = "CHUCK_ENABLED";
    public static final String IS_CHUCK_ENABLED = "is_enable";

    public static boolean isChuckNotifEnabled(Context context) {
        SharedPreferences cache = context.getSharedPreferences(CHUCK_ENABLED, Context.MODE_PRIVATE);
        return cache.getBoolean(IS_CHUCK_ENABLED, false);
    }
}
