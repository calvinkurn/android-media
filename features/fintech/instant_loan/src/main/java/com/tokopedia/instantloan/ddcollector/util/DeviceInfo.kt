package com.tokopedia.instantloan.ddcollector.util

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import com.tokopedia.instantloan.ddcollector.DDConstants.Constant.NOT_AVAILABLE


/**
 * Utility class for fetching the device information
 */
object DeviceInfo {

    val osName: String
        get() {
            try {
                val fields = Build.VERSION_CODES::class.java.fields
                return fields[Build.VERSION.SDK_INT + 1].name
            } catch (e: Exception) {
            }

            return NOT_AVAILABLE
        }

    val systemLanguage: String
        get() = Resources.getSystem().configuration.locale.language

    fun getDeviceName(context: Context): String? {
        var deviceName: String? = NOT_AVAILABLE

        try {
            deviceName = BluetoothAdapter.getDefaultAdapter().name
        } catch (e: Exception) {
            Log.d("Exception", "Error while retrieving device name from bluetooth adapter")
        }

        if (deviceName == null || deviceName.isEmpty()) {
            deviceName = Settings.Secure.getString(context.contentResolver, "bluetooth_name")
        }

        if (deviceName == null || deviceName.isEmpty()) {
            deviceName = Settings.System.getString(context.contentResolver, "device_name")
        }

        return deviceName
    }

    fun getDeviceModelNumber(context: Context): String {
        return if (Build.MODEL == null || Build.MODEL.trim { it <= ' ' }.isEmpty()) {
            NOT_AVAILABLE
        } else Build.MODEL

    }

    fun getDeviceId(context: Context): String = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)


    @SuppressLint("MissingPermission")
    fun getImei(context: Context): String {
        try {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return telephonyManager.deviceId
        } catch (e: Exception) {
            return NOT_AVAILABLE
        }

    }
}