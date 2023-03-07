package com.tokopedia.createpost.view.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData

class ConnectionLiveData(context: Context) : LiveData<Boolean>() {

    private val appContext = context.applicationContext

    private val connectivityManager =
        appContext.getSystemService(CONNECTIVITY_SERVICE) as? ConnectivityManager

    private val connectivityManagerCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            postValue(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            postValue(false)
        }
    }

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateConnection()
        }
    }

    private fun updateConnection() {
        postValue(isNetworkAvailable())
    }

    override fun onActive() {
        super.onActive()
        updateConnection()
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                registerDefaultNetworkCallback()
            }
            Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP -> {
                lollipopNetworkAvailableRequest()
            }
            else -> appContext.registerReceiver(
                networkReceiver,
                IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
            )
        }
    }

    override fun onInactive() {
        super.onInactive()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            unRegisterNetworkCallback()
        } else {
            appContext.unregisterReceiver(networkReceiver)
        }
    }

    private fun lollipopNetworkAvailableRequest() {
        val builder = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        connectivityManager?.registerNetworkCallback(
            builder.build(),
            connectivityManagerCallback
        )
    }

    private fun isNetworkAvailable(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager?.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager?.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun registerDefaultNetworkCallback() {
        connectivityManager?.registerDefaultNetworkCallback(connectivityManagerCallback)
    }

    private fun unRegisterNetworkCallback() {
        connectivityManager?.unregisterNetworkCallback(connectivityManagerCallback)
    }
}
