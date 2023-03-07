package com.tokopedia.fcmcommon.utils

import java.net.NetworkInterface

class FcmDeviceConfig() {

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

    companion object{
        val UNKNOWN = "UNKNOWN"

        fun getFcmDeviceConfig() : FcmDeviceConfig{
            return FcmDeviceConfig()
        }
    }
}
