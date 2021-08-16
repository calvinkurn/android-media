package com.tokopedia.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.tokopedia.device.info.DeviceInfo;

@Deprecated
public class PasswordGenerator {

    private static final String PG_STORAGE = "PG_STORAGE";
    private static final String APP_ID_KEY = "APP_ID";

    public static String getAppId(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(PG_STORAGE, Context.MODE_PRIVATE);
        String appId = sharedPrefs.getString(APP_ID_KEY, null);
        if (appId == null) {
            appId = sharedPrefs.getString(APP_ID_KEY, null);
            if (appId == null) {
                appId = DeviceInfo.getUUID(context);
                Editor editor = sharedPrefs.edit();
                editor.putString(APP_ID_KEY, appId);
                editor.apply();
            }
        }
        return appId;
    }

    public static void clearTokenStorage(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(PG_STORAGE, Context.MODE_PRIVATE);
        sharedPrefs.edit().clear().apply();
    }


}
