package com.tokopedia.media.common.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Build
import android.telephony.TelephonyManager
import com.tokopedia.media.common.data.HIGH_QUALITY
import com.tokopedia.media.common.data.LOW_QUALITY
import com.tokopedia.media.common.data.UNDEFINED
import com.tokopedia.media.common.state.Fast
import com.tokopedia.media.common.state.Low
import com.tokopedia.media.common.state.NetworkState
import com.tokopedia.media.common.state.Undefined

object NetworkManager {

    private const val NOT_CONNECTED = -1
    private const val WIFI = 1
    private const val MOBILE = 2

    private const val CONN_UNKNOWN = "unknown"
    private const val CONN_WIFI = "wifi"
    private const val CONN_2G = "2g"
    private const val CONN_3G = "3g"
    private const val CONN_4G = "4g"
    private const val CONN_5G = "5g"

    private const val NETWORK_TYPE_LTE_CA = 19

    fun state(context: Context?): String {
        if (context == null) return UNDEFINED

        return when (networkState(context)) {
            Low -> LOW_QUALITY
            Fast -> HIGH_QUALITY
            else -> UNDEFINED
        }
    }

    private fun networkState(context: Context): NetworkState {
        val connectionType = getConnectionType(context)
        return if (connectionType == CONN_2G || connectionType == CONN_3G) {
            Low
        } else if (connectionType == CONN_4G
            || connectionType == CONN_5G
            || connectionType == CONN_WIFI) {
            Fast
        } else {
            Undefined
        }
    }

    private fun getConnectionTransportType(context: Context): Int {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(TRANSPORT_WIFI)) {
                    return WIFI
                } else if (capabilities.hasTransport(TRANSPORT_CELLULAR)) {
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
                        TelephonyManager.NETWORK_TYPE_GPRS, // 2G ~ 100 kbps
                        TelephonyManager.NETWORK_TYPE_1xRTT, // 2G ~ 50-100 kbps
                        TelephonyManager.NETWORK_TYPE_CDMA, // 2G ~ 14-64 kbps
                        TelephonyManager.NETWORK_TYPE_EDGE, // 2G ~ 50-100 kbps
                        TelephonyManager.NETWORK_TYPE_IDEN, // 2G ~ 25 kbps
                        TelephonyManager.NETWORK_TYPE_GSM -> CONN_2G
                        TelephonyManager.NETWORK_TYPE_UMTS, // 3G ~ 400-7000 kbps
                        TelephonyManager.NETWORK_TYPE_EVDO_0, // 3G ~ 400-1000 kbps
                        TelephonyManager.NETWORK_TYPE_EVDO_A, // 3G ~ 600-1400 kbps
                        TelephonyManager.NETWORK_TYPE_HSDPA, // 3G ~ 2-14 Mbps
                        TelephonyManager.NETWORK_TYPE_HSPA, // 3G ~ 700-1700 kbps
                        TelephonyManager.NETWORK_TYPE_HSUPA, // 3G ~ 1-23 Mbps
                        TelephonyManager.NETWORK_TYPE_EHRPD, // 3G ~ 1-2 Mbps
                        TelephonyManager.NETWORK_TYPE_EVDO_B, // 3G ~ 5 Mbps
                        TelephonyManager.NETWORK_TYPE_HSPAP, // 3G ~ 10-20 Mbps
                        TelephonyManager.NETWORK_TYPE_TD_SCDMA -> CONN_3G
                        TelephonyManager.NETWORK_TYPE_LTE -> CONN_4G // 4G ~ 10+ Mbps
                        NETWORK_TYPE_LTE_CA -> CONN_4G // LTE CA
                        TelephonyManager.NETWORK_TYPE_NR -> CONN_5G  //5G
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