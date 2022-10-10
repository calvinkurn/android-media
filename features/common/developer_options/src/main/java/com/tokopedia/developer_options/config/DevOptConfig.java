package com.tokopedia.developer_options.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created By : Jonathan Darwin on September 29, 2022
 */
public class DevOptConfig {

    private static final String CHUCK_ENABLED = "CHUCK_ENABLED";
    private static final String IS_CHUCK_ENABLED = "is_enable";

    private static final String DEV_OPT_ON_NOTIF_ENABLED = "DEV_OPT_ON_NOTIF_ENABLED";
    private static final String IS_DEV_OPT_ON_NOTIF_ENABLED = "IS_DEV_OPT_ON_NOTIF_ENABLED";

    public static boolean isChuckNotifEnabled(Context context) {
        SharedPreferences cache = context.getSharedPreferences(CHUCK_ENABLED, Context.MODE_PRIVATE);
        return cache.getBoolean(IS_CHUCK_ENABLED, false);
    }

    public static void setChuckNotifEnabled(Context context, boolean isChuckNotifEnabled) {
        context.getSharedPreferences(CHUCK_ENABLED, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(
                        IS_CHUCK_ENABLED,
                        isChuckNotifEnabled
                )
                .apply();
    }

    public static boolean isDevOptOnNotifEnabled(Context context) {
        SharedPreferences cache = context.getSharedPreferences(DEV_OPT_ON_NOTIF_ENABLED, Context.MODE_PRIVATE);
        return cache.getBoolean(IS_DEV_OPT_ON_NOTIF_ENABLED, true);
    }

    public static void setDevOptOnNotifEnabled(Context context, boolean isDevOptOnNotifEnabled) {
        context.getSharedPreferences(DEV_OPT_ON_NOTIF_ENABLED, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(
                        IS_DEV_OPT_ON_NOTIF_ENABLED,
                        isDevOptOnNotifEnabled
                )
                .apply();
    }
}
