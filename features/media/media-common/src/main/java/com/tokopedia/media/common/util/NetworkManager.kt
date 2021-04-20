package com.tokopedia.media.common.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.net.NetworkInfo
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

    fun state(context: Context?): String {
        return when (networkState(context)) {
            Low -> LOW_QUALITY
            Fast -> HIGH_QUALITY
            else -> UNDEFINED
        }
    }

    private fun networkState(context: Context?): NetworkState? {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager? ?: return Undefined

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkState = connectivityManager.activeNetwork ?: return Undefined
            val capabilities = connectivityManager.getNetworkCapabilities(networkState)?: return Undefined
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> Fast
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> isFast(context)
                else -> Undefined
            }
        } else {
            val networkState = connectivityManager.activeNetworkInfo ?: return Undefined
            if (networkState.isConnected) return Undefined
            return isFast(context)
        }
    }

    private fun isFast(context: Context?): NetworkState {
        val networkInfo = networkInfo(context)?: return Undefined
        return if (isConnectionFast(networkInfo.type, networkInfo.subtype)) Fast else Low
    }

    private fun networkInfo(context: Context?): NetworkInfo? {
        return (context?.getSystemService(
                Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager).activeNetworkInfo
    }

    private fun isConnectionFast(type: Int, subType: Int): Boolean {
        return when (type) {
            ConnectivityManager.TYPE_WIFI -> true
            ConnectivityManager.TYPE_MOBILE -> {
                when (subType) {
                    // 2g
                    TelephonyManager.NETWORK_TYPE_GPRS -> false
                    TelephonyManager.NETWORK_TYPE_EDGE -> false
                    TelephonyManager.NETWORK_TYPE_CDMA -> false
                    TelephonyManager.NETWORK_TYPE_1xRTT -> false
                    TelephonyManager.NETWORK_TYPE_IDEN -> false
                    // 3g
                    TelephonyManager.NETWORK_TYPE_UMTS -> false
                    TelephonyManager.NETWORK_TYPE_EVDO_0 -> false
                    TelephonyManager.NETWORK_TYPE_EVDO_A -> false
                    TelephonyManager.NETWORK_TYPE_HSDPA -> false
                    TelephonyManager.NETWORK_TYPE_HSUPA -> false
                    TelephonyManager.NETWORK_TYPE_HSPA -> false
                    TelephonyManager.NETWORK_TYPE_EVDO_B -> false
                    TelephonyManager.NETWORK_TYPE_EHRPD -> false
                    TelephonyManager.NETWORK_TYPE_HSPAP -> false
                    // 4g
                    TelephonyManager.NETWORK_TYPE_LTE -> true
                    TelephonyManager.NETWORK_TYPE_UNKNOWN -> false
                    else -> false
                }
            }
            else -> false
        }
    }

}