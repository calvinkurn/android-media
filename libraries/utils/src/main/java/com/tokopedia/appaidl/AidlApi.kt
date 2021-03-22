package com.tokopedia.appaidl

import android.content.*
import android.os.Bundle
import com.tokopedia.appaidl.data.CUSTOMER_APP
import com.tokopedia.appaidl.data.SELLER_APP
import com.tokopedia.appaidl.data.tagDefault
import com.tokopedia.appaidl.data.componentTargetName
import com.tokopedia.appaidl.service.AidlServiceConnection

open class AidlApi(
        private val onAidlReceive: (tag: String, bundle: Bundle?) -> Unit,
        private val onAidlError: () -> Unit
) {

    private var stubService: AidlServiceConnection? = null
    private var receiver: BroadcastReceiver? = null

    private fun broadcastReceiver(): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                // only receive the data in both of customerApp and sellerApp
                if (intent?.action == tagDefault()) {
                    intent.action?.let {
                        onAidlReceive(it, intent.extras)
                    }
                }

                // indicate the receiver on `onReceive()`
                receiver = this
            }
        }
    }

    fun bindService(context: Context, tag: String = tagDefault(), serviceName: String) {
        // the serviceView is serviceConnection to register the receiver in activity and send the data
        stubService = AidlServiceConnection { service ->
            context.registerReceiver(broadcastReceiver(), IntentFilter().apply { addAction(tag) })

            // send the data once the serviceConnection is connected
            service?.send(tag)
        }.also {
            /*
            * preparing the data would be send to another app using aidlRemoteService
            * with componentTargetName as entities of package name.
            *
            * componentTargetName() should be same with broadcastResult() on aidlRemoteService.
            * */
            val success = context.bindService(Intent().apply {
                component = ComponentName(componentTargetName(), serviceName)
            }, it, Context.BIND_AUTO_CREATE)

            // handling error
            if (!success) onAidlError()
        }
    }

    fun unbindService(context: Context) {
        stubService?.let { context.unbindService(it) }
        receiver?.let { context.unregisterReceiver(it) }
    }

}