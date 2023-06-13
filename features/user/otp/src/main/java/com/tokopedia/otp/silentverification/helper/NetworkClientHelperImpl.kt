package com.tokopedia.otp.silentverification.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import com.tokopedia.otp.silentverification.view.NetworkRequestListener

/**
 * Created by Yoris on 02/11/21.
 */

class NetworkClientHelperImpl (val cellularNetworkRequest: NetworkRequest): NetworkClientHelper {

    companion object {
        const val SILENT_VERIF_TIMEOUT = 10L
    }

    override fun makeNetworkRequest(context: Context, listener: NetworkRequestListener) {
        val connectivityManager =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.requestNetwork(cellularNetworkRequest,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    listener.onSuccess(network)
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    listener.onError(Throwable("Network Unavailable"))
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    listener.onError(Throwable("Network Lost"))
                }
            })
    }
}