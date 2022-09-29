package com.tokopedia.developer_options.config;

import android.content.Context;
import android.content.SharedPreferences;

public class DevOptConfig {

    public static final String CHUCK_ENABLED = "CHUCK_ENABLED";
    public static final String IS_CHUCK_ENABLED = "is_enable";

    public static final String DEV_OPT_ON_NOTIF_ENABLED = "DEV_OPT_ON_NOTIF_ENABLED";
    public static final String IS_DEV_OPT_ON_NOTIF_ENABLED = "IS_DEV_OPT_ON_NOTIF_ENABLED";

    public static boolean isChuckNotifEnabled(Context context) {
        SharedPreferences cache = context.getSharedPreferences(CHUCK_ENABLED, Context.MODE_PRIVATE);
        return cache.getBoolean(IS_CHUCK_ENABLED, false);
    }

    public static boolean isDevOptOnNotifEnabled(Context context) {
        SharedPreferences cache = context.getSharedPreferences(DEV_OPT_ON_NOTIF_ENABLED, Context.MODE_PRIVATE);
        return cache.getBoolean(IS_DEV_OPT_ON_NOTIF_ENABLED, false);
    }

    public static void setDevOptOnNotifEnabled(Context context, boolean isDevOptOnNotifEnabled) {
        context.getSharedPreferences(DEV_OPT_ON_NOTIF_ENABLED, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(
                DevOptConfig.IS_DEV_OPT_ON_NOTIF_ENABLED,
                isDevOptOnNotifEnabled
            )
            .apply();
    }
}
