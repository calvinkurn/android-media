package com.tokopedia.instantloan.ddcollector.util;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Field;

import static com.tokopedia.instantloan.ddcollector.DDConstants.NOT_AVAILABLE;

/**
 * Utility class for fetching the device information
 */
public class DeviceInfo {
    public static String getDeviceName(Context context) {
        String deviceName = NOT_AVAILABLE;

        try {
            deviceName = BluetoothAdapter.getDefaultAdapter().getName();
        } catch (Exception e) {
            Log.d("Exception", "Error while retrieving device name from bluetooth adapter");
        }

        if (deviceName == null || deviceName.isEmpty()) {
            deviceName = Settings.Secure.getString(context.getContentResolver(), "bluetooth_name");
        }

        if (deviceName == null || deviceName.isEmpty()) {
            deviceName = Settings.System.getString(context.getContentResolver(), "device_name");
        }

        return deviceName;
    }

    public static String getDeviceModelNumber(Context context) {
        if (Build.MODEL == null || Build.MODEL.trim().isEmpty()) {
            return NOT_AVAILABLE;
        }

        return Build.MODEL;
    }

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getOSName() {
        try {
            Field[] fields = Build.VERSION_CODES.class.getFields();
            return fields[Build.VERSION.SDK_INT + 1].getName();
        } catch (Exception e) {
            Log.e("Exception", "Error while retrieving os name ", e);
        }

        return NOT_AVAILABLE;
    }

    @SuppressLint("MissingPermission")
    public static String getImei(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
            return NOT_AVAILABLE;
        }
    }

    public static String getSystemLanguage() {
        return Resources.getSystem().getConfiguration().locale.getLanguage();
    }
}