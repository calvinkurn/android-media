package com.tokopedia.sellerapp.utils;

import android.content.Context;
import android.provider.Settings;

public class DeviceUtil {
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
