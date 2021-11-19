package com.tokopedia.media.loader.tracker

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.telephony.TelephonyManager

object DeviceNetworkInfo {

    private const val NOT_CONNECTED = -1
    private const val WIFI = 1
    private const val MOBILE = 2

    private const val CONN_UNKNOWN = "unknown"
    private const val CONN_WIFI = "wifi"
    private const val CONN_2G = "2g"
    private const val CONN_3G = "3g"
    private const val CONN_4G = "4g"
    private const val CONN_5G = "5g"

    private fun getConnectionTransportType(context: Context): Int {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return WIFI
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return MOBILE
                }
            }
        } else {
            val activeNetwork = cm.activeNetworkInfo
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                    return WIFI
                } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                    return MOBILE
                }
            }
        }
        return NOT_CONNECTED
    }

    @JvmStatic
    fun getConnectionType(context: Context): String {
        return try {
            when (getConnectionTransportType(context)) {
                WIFI -> CONN_WIFI
                MOBILE -> {
                    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    if (cm.activeNetworkInfo?.subtype == null) return CONN_UNKNOWN
                    return when (cm.activeNetworkInfo?.subtype) {
                        // 2G:
                        TelephonyManager.NETWORK_TYPE_GPRS, // ~ 100 kbps
                        TelephonyManager.NETWORK_TYPE_1xRTT, // ~ 50-100 kbps
                        TelephonyManager.NETWORK_TYPE_CDMA, // ~ 14-64 kbps
                        TelephonyManager.NETWORK_TYPE_EDGE, // ~ 50-100 kbps
                        TelephonyManager.NETWORK_TYPE_IDEN, // ~25 kbps
                        TelephonyManager.NETWORK_TYPE_GSM -> CONN_2G

                        // 3G
                        TelephonyManager.NETWORK_TYPE_UMTS, // ~ 400-7000 kbps
                        TelephonyManager.NETWORK_TYPE_EVDO_0, // ~ 400-1000 kbps
                        TelephonyManager.NETWORK_TYPE_EVDO_A, // ~ 600-1400 kbps
                        TelephonyManager.NETWORK_TYPE_HSDPA, // ~ 2-14 Mbps
                        TelephonyManager.NETWORK_TYPE_HSPA, // ~ 700-1700 kbps
                        TelephonyManager.NETWORK_TYPE_HSUPA, // ~ 1-23 Mbps
                        TelephonyManager.NETWORK_TYPE_EHRPD, // ~ 1-2 Mbps
                        TelephonyManager.NETWORK_TYPE_EVDO_B, // ~ 5 Mbps
                        TelephonyManager.NETWORK_TYPE_HSPAP, // ~ 10-20 Mbps
                        TelephonyManager.NETWORK_TYPE_TD_SCDMA -> CONN_3G

                        //4G
                        TelephonyManager.NETWORK_TYPE_LTE -> CONN_4G // ~ 10+ Mbps

                        // LTE CA
                        19 -> CONN_4G

                        TelephonyManager.NETWORK_TYPE_NR -> CONN_5G

                        //5G
                        20 -> CONN_5G

                        TelephonyManager.NETWORK_TYPE_UNKNOWN -> CONN_UNKNOWN
                        else -> CONN_UNKNOWN
                    }
                }
                else -> {
                    CONN_UNKNOWN
                }
            }
        } catch (e:Exception) {
            return CONN_UNKNOWN
        }
    }

}