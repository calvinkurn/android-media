package com.tokopedia.appaidl

import android.content.*
import android.os.Bundle
import com.tokopedia.appaidl.data.*
import com.tokopedia.appaidl.ui.ServiceView

open class AidlApi(
        private val context: Context,
        private val listener: ReceiverListener
) {

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == CUSTOMER_APP || intent?.action == SELLER_APP) {
                intent.action?.let {
                    this@AidlApi.context.unregisterReceiver(this)
                    listener.onAidlReceive(it, intent.extras)
                }
            }
        }
    }

    fun bindAidlService() {
        // the serviceView is serviceConnection to register the receiver in activity and send the data
        val serviceView = ServiceView(aidlTag()) { tag, service ->
            if (service != null) {
                context.registerReceiver(broadcastReceiver, IntentFilter().apply { addAction(tag) })
                service.send(tag)
            }
        }

        val success = context.bindService(Intent().apply {
            component = ComponentName(componentTargetName(), REMOTE_SERVICE)
        }, serviceView, Context.BIND_AUTO_CREATE)

        if (!success) listener.onAidlError()
    }

    interface ReceiverListener {
        fun onAidlReceive(tag: String, bundle: Bundle?)
        fun onAidlError()
    }

}