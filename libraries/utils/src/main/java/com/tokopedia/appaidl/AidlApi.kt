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

    private fun broadcastReceiver(_context: Context): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                // only receive the data in both of customerApp and sellerApp
                if (intent?.action == CUSTOMER_APP || intent?.action == SELLER_APP) {
                    intent.action?.let {
                        onAidlReceive(it, intent.extras)

                        // every data has received, unregister it
                        _context.unregisterReceiver(this)
                    }
                }
            }
        }
    }

    fun bindService(context: Context, tag: String = tagDefault(), serviceName: String) {
        // the serviceView is serviceConnection to register the receiver in activity and send the data
        stubService = AidlServiceConnection { service ->
            context.registerReceiver(broadcastReceiver(context), IntentFilter().apply { addAction(tag) })

            // send the data once the serviceConnection is connected
            service?.send(tag)
        }.also {
            /*
            * preparing the data would be send to another app using aidlRemoteService
            * with componentTargetName as entities of package name.
            *
            * componentTargetName() should be same with broadcastResult() on aidlRemoteService.
            * */
            val intent = Intent().apply { component = ComponentName(componentTargetName(), serviceName) }
            val success = context.bindService(intent, it, Context.BIND_AUTO_CREATE)

            // handling error
            if (!success) onAidlError()
        }
    }

    fun unbindService(context: Context) {
        stubService?.let { context.unbindService(it) }
    }

}