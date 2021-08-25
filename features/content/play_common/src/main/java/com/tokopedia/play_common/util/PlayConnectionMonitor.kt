package com.tokopedia.play_common.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


/**
 * Created by mzennis on 2020-01-28.
 */
class PlayConnectionMonitor(context: Context) {

    private val connectivityManager =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _observablePlayConnectionState = MutableLiveData<PlayConnectionState>()

    private var networkCallback: ConnectivityManager.NetworkCallback?= null

    init {
        try {
            val nc = object : ConnectivityManager.NetworkCallback() {
                override fun onLost(network: Network) {
                    _observablePlayConnectionState.postValue(PlayConnectionState.UnAvailable)
                }
                override fun onUnavailable() {
                    _observablePlayConnectionState.postValue(PlayConnectionState.UnAvailable)
                }
                override fun onLosing(network: Network, maxMsToLive: Int) {
                    _observablePlayConnectionState.postValue(PlayConnectionState.UnAvailable)
                }
                override fun onAvailable(network: Network) {
                    _observablePlayConnectionState.postValue(PlayConnectionState.Available)
                }
            }
            networkCallback = nc
            val networkRequest =
                    NetworkRequest.Builder()
                            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                            .build()
            connectivityManager.registerNetworkCallback(networkRequest, nc)
        } catch (ignored: Throwable) {}
    }

    fun getObservablePlayConnectionState(): LiveData<PlayConnectionState> = _observablePlayConnectionState

    fun end() {
        networkCallback?.let { it ->
            try {
                connectivityManager.unregisterNetworkCallback(it)
            } catch (e: Exception) { }
        }
    }
}