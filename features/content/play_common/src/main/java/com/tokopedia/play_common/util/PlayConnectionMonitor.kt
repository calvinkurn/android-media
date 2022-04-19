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

    private val networkCallback: ConnectivityManager.NetworkCallback = object : ConnectivityManager.NetworkCallback() {
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

        init {
            val networkRequest = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build()
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        }

        fun getObservablePlayConnectionState(): LiveData<PlayConnectionState> = _observablePlayConnectionState

        fun end() {
            try {
                connectivityManager.unregisterNetworkCallback(networkCallback)
            } catch (e: Exception) { }
        }
}