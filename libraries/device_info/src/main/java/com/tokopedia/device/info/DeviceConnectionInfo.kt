package com.tokopedia.device.info

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


    const val NOT_CONNECTED = -1
    const val WIFI = 1
    const val MOBILE = 2

    const val CONN_WIFI = "WI-FI"
    const val CONN_RTT = "1xRTT"
    const val CONN_CDMA = "CDMA"
    const val CONN_EDGE = "EDGE"
    const val CONN_EHRPD = "eHRPD"
    const val CONN_EVDO_0 = "EVDO_0"
    const val CONN_EVDO_A = "EVDO_A"
    const val CONN_EVDO_B = "EVDO_B"
    const val CONN_GPRS = "GPRS"
    const val CONN_GSM = "GSM"
    const val CONN_HSDPA = "HSDPA"
    const val CONN_HSPA = "HSPA"
    const val CONN_HSPAP = "HSPA+"
    const val CONN_HSUPA = "HSUPA"
    const val CONN_IDEN = "iDen"
    const val CONN_UMTS = "UMTS"
    const val CONN_LTE = "LTE"
    const val CONN_LTE_CA = "LTE_CA"
    const val CONN_NR = "NR"
    const val CONN_TD_SCDMA = "TD_SCDMA"
    const val CONN_IWLAN = "IWLAN"
    const val CONN_UNKNOWN = "unknown"

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
            val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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

        return ssid.replace("\"", "")
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
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
    fun getConnectionType(context: Context): String? {
        return when (getConnectionTransportType(context)) {
            WIFI -> CONN_WIFI
            MOBILE -> {
                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                return when (cm.activeNetworkInfo.subtype) {
                    // 2G:
                    TelephonyManager.NETWORK_TYPE_GPRS -> CONN_GPRS // ~ 100 kbps
                    TelephonyManager.NETWORK_TYPE_1xRTT -> CONN_RTT // ~ 50-100 kbps
                    TelephonyManager.NETWORK_TYPE_CDMA -> CONN_CDMA // ~ 14-64 kbps
                    TelephonyManager.NETWORK_TYPE_EDGE -> CONN_EDGE // ~ 50-100 kbps
                    TelephonyManager.NETWORK_TYPE_IDEN -> CONN_IDEN // ~25 kbps
                    TelephonyManager.NETWORK_TYPE_GSM -> CONN_GSM

                    // 3G
                    TelephonyManager.NETWORK_TYPE_UMTS -> CONN_UMTS // ~ 400-7000 kbps
                    TelephonyManager.NETWORK_TYPE_EVDO_0 -> CONN_EVDO_0 // ~ 400-1000 kbps
                    TelephonyManager.NETWORK_TYPE_EVDO_A -> CONN_EVDO_A // ~ 600-1400 kbps
                    TelephonyManager.NETWORK_TYPE_HSDPA -> CONN_HSDPA // ~ 2-14 Mbps
                    TelephonyManager.NETWORK_TYPE_HSPA -> CONN_HSPA // ~ 700-1700 kbps
                    TelephonyManager.NETWORK_TYPE_HSUPA -> CONN_HSUPA // ~ 1-23 Mbps
                    TelephonyManager.NETWORK_TYPE_EHRPD -> CONN_EHRPD // ~ 1-2 Mbps
                    TelephonyManager.NETWORK_TYPE_EVDO_B -> CONN_EVDO_B // ~ 5 Mbps
                    TelephonyManager.NETWORK_TYPE_HSPAP -> CONN_HSPAP // ~ 10-20 Mbps
                    TelephonyManager.NETWORK_TYPE_TD_SCDMA -> CONN_TD_SCDMA

                    //4G
                    TelephonyManager.NETWORK_TYPE_LTE -> CONN_LTE // ~ 10+ Mbps
                    TelephonyManager.NETWORK_TYPE_IWLAN-> CONN_IWLAN
                    // LTE CA
                    19 -> CONN_LTE_CA

                    //5G
                    20 -> CONN_NR

                    TelephonyManager.NETWORK_TYPE_UNKNOWN -> CONN_UNKNOWN
                    else -> CONN_UNKNOWN
                }
            }
            else -> {
                CONN_UNKNOWN
            }
        }
    }
}
