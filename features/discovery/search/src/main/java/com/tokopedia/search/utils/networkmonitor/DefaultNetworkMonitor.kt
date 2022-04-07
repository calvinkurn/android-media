package com.tokopedia.search.utils.networkmonitor

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.device.info.DeviceConnectionInfo
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber

internal class DefaultNetworkMonitor(
    context: Context?,
    lifecycleOwner: LifecycleOwner?
) : NetworkMonitor,
    ContextProvider by WeakReferenceContextProvider(context),
    LifecycleObserver {

    private val connectedToWifiState : MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // Network capabilities have changed for the network
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val onWifi = isConnectedToWifi()
            onWifiConnectionChange(onWifi)
        }

        private fun isConnectedToWifi(): Boolean {
            val currentContext = this@DefaultNetworkMonitor.context ?: return false
            return DeviceConnectionInfo.isConnectWifi(currentContext)
        }
    }

    init {
        lifecycleOwner?.let { registerLifecycle(it) }
    }

    private fun registerLifecycle(lifecycleOwner: LifecycleOwner) {
        if(lifecycleOwner is Fragment) {
            lifecycleOwner.viewLifecycleOwnerLiveData.observe(lifecycleOwner, Observer {
                it.lifecycle.addObserver(this)
            })
        } else {
            lifecycleOwner.lifecycle.addObserver(this)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onViewCreated() {
        registerNetworkCallback()
    }

    private fun registerNetworkCallback() {
        val context = context ?: return
        val connectivityManager = ContextCompat.getSystemService(context, ConnectivityManager::class.java) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder().build()
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                connectivityManager.registerDefaultNetworkCallback(networkCallback)
            } else {
                connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onViewDestroyed() {
        unregisterNetworkCallback()
    }

    private fun unregisterNetworkCallback() {
        val context = context ?: return
        val connectivityManager = ContextCompat.getSystemService(context, ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun onWifiConnectionChange(connectedToWifi: Boolean) {
        connectedToWifiState.value = connectedToWifi
    }

    override val wifiConnectionState: Flow<Boolean>
        get() = connectedToWifiState.distinctUntilChanged { old, new ->
            old == new
        }
}