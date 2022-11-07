package com.tokopedia.test.application.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.telephony.TelephonyManager
import android.text.TextUtils
import java.net.NetworkInterface
import java.util.*

object DeviceConnectionInfo {

    /**
     * Return SSID device
     * The function will return empty string when device can not get the ssid
     *
     * @param context an android.content.Context
     */
    @JvmStatic
    fun getSSID(context: Context): String {
        var ssid = ""
        try {
            val connManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connManager.activeNetworkInfo
            if (networkInfo != null && networkInfo.isConnected &&
                    networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val connectionInfo = wifiManager.connectionInfo
                if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.ssid)) {
                    ssid = connectionInfo.ssid
                }
            }
        } catch (e: Exception) {
            return ""
        }

        return ssid
    }

    @JvmStatic
    fun isConnectWifi(context: Context): Boolean {
        return isInternetAvailable(context, checkWifi = true)
    }

    @JvmStatic
    fun isConnectCellular(context: Context): Boolean {
        return isInternetAvailable(context, checkCellular = true)
    }

    @JvmStatic
    fun isConnectEthernet(context: Context): Boolean {
        return isInternetAvailable(context, checkEthernet = true)
    }

    @JvmStatic
    fun isInternetAvailable(context: Context,
                            checkWifi: Boolean = false,
                            checkCellular: Boolean = false,
                            checkEthernet: Boolean = false): Boolean {
        var result = false
        val connectivityManager =
                context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            when {
                checkWifi -> return actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                checkCellular -> return actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                checkEthernet -> return actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                else -> {
                    result = when {
                        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }


        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    when {
                        checkWifi -> return type == ConnectivityManager.TYPE_WIFI
                        checkCellular -> return  type == ConnectivityManager.TYPE_MOBILE
                        checkEthernet -> return  type == ConnectivityManager.TYPE_ETHERNET
                        else -> {
                            result = when (type) {
                                ConnectivityManager.TYPE_WIFI -> true
                                ConnectivityManager.TYPE_MOBILE -> true
                                ConnectivityManager.TYPE_ETHERNET -> true
                                else -> false
                            }
                        }
                    }

                }
            }
        }

        return result
    }

    @JvmStatic
    fun getHttpAgent(): String {
        return System.getProperty("http.agent").orEmpty()
    }

    @JvmStatic
    fun getCarrierName(context: Context): String {
        val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return manager.networkOperatorName
    }

    @JvmStatic
    fun getIPAddress(useIPv4: Boolean): String {
        try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress
                        val isIPv4 = sAddr.indexOf(':') < 0

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                                return if (delim < 0) sAddr.toUpperCase() else sAddr.substring(0, delim).toUpperCase()
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
        }
        // for now eat exceptions
        return ""
    }
}