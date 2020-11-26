package com.tkpd.library.utils.legacy;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import timber.log.Timber;

@Deprecated
public class CommonUtils {

    public CommonUtils() {

    }

    public static String getUniqueDeviceID(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String DeviceID = tm.getDeviceId();
        String Brand = Build.BRAND;
        String Model = Build.MODEL;
        String UniqueDeviceID = Brand + "~" + Model + "~" + DeviceID;
        Timber.d("Device ID" + UniqueDeviceID);
        return UniqueDeviceID;
    }

}