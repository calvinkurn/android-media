package com.tokopedia.notifications.utils

import android.content.Context
import android.content.res.Configuration
import java.net.NetworkInterface

class CMDeviceConfig() {

    fun getWifiMAC(): String {
        try {
            for (nif in NetworkInterface.getNetworkInterfaces()) {
                if (!nif.name.equals("wlan0", ignoreCase = true)) continue
                val macBytes = nif.hardwareAddress ?: return UNKNOWN
                val result = StringBuilder()
                for (b in macBytes) {
                    result.append(String.format("%02X:", b))
                }
                if (result.isNotEmpty()) {
                    result.deleteCharAt(result.length - 1)
                }
                return result.toString()
            }
        } catch (ex: Exception) {
            return UNKNOWN
        }
        return UNKNOWN
    }

    fun isDarkMode(context: Context): Boolean {
        return try {
            when (context.resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> true
                Configuration.UI_MODE_NIGHT_NO -> false
                Configuration.UI_MODE_NIGHT_UNDEFINED -> false
                else -> false
            }
        } catch (ignored: Exception) {
            false
        }
    }

    companion object{
        val UNKNOWN = "UNKNOWN"

        fun getCMDeviceConfig() : CMDeviceConfig{
            return CMDeviceConfig()
        }
    }
}