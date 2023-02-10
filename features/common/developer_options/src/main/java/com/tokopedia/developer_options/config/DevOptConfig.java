package com.tokopedia.developer_options.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created By : Jonathan Darwin on September 29, 2022
 */
public class DevOptConfig {

    private static final String CHUCKER_CONFIG = "chucker_config";
    private static final String IS_CHUCKER_NORIFICATION_ENABLED = "is_chucker_notification_enabled";

    private static final String DEV_OPT_ON_NOTIF_ENABLED = "DEV_OPT_ON_NOTIF_ENABLED";
    private static final String IS_DEV_OPT_ON_NOTIF_ENABLED = "IS_DEV_OPT_ON_NOTIF_ENABLED";

    public static boolean isChuckNotifEnabled(Context context) {
        SharedPreferences cache = context.getSharedPreferences(CHUCKER_CONFIG, Context.MODE_PRIVATE);
        return cache.getBoolean(IS_CHUCKER_NORIFICATION_ENABLED, false);
    }

    public static void setChuckNotifEnabled(Context context, boolean isChuckNotifEnabled) {
        context.getSharedPreferences(CHUCKER_CONFIG, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(
                    IS_CHUCKER_NORIFICATION_ENABLED,
                        isChuckNotifEnabled
                )
                .apply();
    }

    public static boolean isDevOptOnNotifEnabled(Context context) {
        SharedPreferences cache = context.getSharedPreferences(DEV_OPT_ON_NOTIF_ENABLED, Context.MODE_PRIVATE);
        return cache.getBoolean(IS_DEV_OPT_ON_NOTIF_ENABLED, false);
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
