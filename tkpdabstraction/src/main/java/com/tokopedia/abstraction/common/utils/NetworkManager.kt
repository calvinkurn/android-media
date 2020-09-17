package com.tokopedia.abstraction.common.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.net.NetworkInfo
import android.os.Build
import android.telephony.TelephonyManager

sealed class NetworkState
object Fast: NetworkState()
object Low: NetworkState()
object Undefined: NetworkState()

object NetworkManager {

    private const val low = "2g"
    private const val fast = "4g"
    private const val undefined = "undefined"

    fun state(context: Context): String {
        return when (networkState(context)) {
            Low -> low
            Fast -> fast
            else -> undefined
        }
    }

    private fun networkState(context: Context): NetworkState {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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

    private fun isFast(context: Context): NetworkState {
        val networkInfo = networkInfo(context)?: return Undefined
        return if (isConnectionFast(networkInfo.type, networkInfo.subtype)) Fast else Low
    }

    private fun networkInfo(context: Context): NetworkInfo? {
        return (context.getSystemService(
                Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager).activeNetworkInfo
    }

    private fun isConnectionFast(type: Int, subType: Int): Boolean {
        return when (type) {
            ConnectivityManager.TYPE_WIFI -> true
            ConnectivityManager.TYPE_MOBILE -> {
                when (subType) {
                    TelephonyManager.NETWORK_TYPE_1xRTT -> false // ~ 50-100 kbps
                    TelephonyManager.NETWORK_TYPE_CDMA -> false // ~ 14-64 kbps
                    TelephonyManager.NETWORK_TYPE_EDGE -> false // ~ 50-100 kbps
                    TelephonyManager.NETWORK_TYPE_EVDO_0 -> false // ~ 400-1000 kbps
                    TelephonyManager.NETWORK_TYPE_EVDO_A -> false // ~ 600-1400 kbps
                    TelephonyManager.NETWORK_TYPE_GPRS -> false // ~ 100 kbps
                    TelephonyManager.NETWORK_TYPE_HSDPA -> true // ~ 2-14 Mbps
                    TelephonyManager.NETWORK_TYPE_HSPA -> false // ~ 700-1700 kbps
                    TelephonyManager.NETWORK_TYPE_HSUPA -> true // ~ 1-23 Mbps
                    TelephonyManager.NETWORK_TYPE_UMTS -> false // ~ 400-7000 kbps
                    TelephonyManager.NETWORK_TYPE_EHRPD -> false // ~ 1-2 Mbps
                    TelephonyManager.NETWORK_TYPE_EVDO_B -> false // ~ 5 Mbps
                    TelephonyManager.NETWORK_TYPE_HSPAP -> false // ~ 10-20 Mbps
                    TelephonyManager.NETWORK_TYPE_IDEN -> false // ~25 kbps
                    TelephonyManager.NETWORK_TYPE_LTE -> true // ~ 10+ Mbps
                    TelephonyManager.NETWORK_TYPE_UNKNOWN -> false
                    else -> false
                }
            }
            else -> false
        }
    }

}