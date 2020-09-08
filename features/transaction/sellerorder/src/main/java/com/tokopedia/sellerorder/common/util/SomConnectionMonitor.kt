package com.tokopedia.sellerorder.common.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build

class SomConnectionMonitor(context: Context) {

    private val connectivityManager =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    var isConnected: Boolean = true

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                networkCallback = object : ConnectivityManager.NetworkCallback() {
                    override fun onLost(network: Network?) {
                        isConnected = false
                    }

                    override fun onUnavailable() {
                        isConnected = false
                    }

                    override fun onLosing(network: Network?, maxMsToLive: Int) {
                        isConnected = false
                    }

                    override fun onAvailable(network: Network?) {
                        isConnected = true
                    }
                }
                val networkRequest = NetworkRequest.Builder()
                        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                        .build()

                networkCallback?.run {
                    connectivityManager.registerNetworkCallback(networkRequest, this)
                }
            } catch (ignored: Throwable) {
            }
        }
    }

    fun end() {
        if (networkCallback != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                networkCallback?.run {
                    connectivityManager.unregisterNetworkCallback(this)
                }
            } catch (ignored: Exception) {
            }
        }
    }
}