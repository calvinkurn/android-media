package com.tokopedia.play_common.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import java.lang.Exception

object PlayConnectionCommon {

    @JvmStatic
    fun isConnectWifi(context: Context): Boolean {
        return isInternetAvailable(context, checkWifi = true)
    }

    @JvmStatic
    fun isConnectCellular(context: Context): Boolean {
        return isInternetAvailable(context, checkCellular = true)
    }


    @JvmStatic
    fun isInternetAvailable(context: Context,
                            checkWifi: Boolean = false,
                            checkCellular: Boolean = false,
                            checkEthernet: Boolean = false): Boolean {
        return try {
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
                            checkCellular -> return type == ConnectivityManager.TYPE_MOBILE
                            checkEthernet -> return type == ConnectivityManager.TYPE_ETHERNET
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
            result
        } catch (e: Exception){
            false
        }
    }
}