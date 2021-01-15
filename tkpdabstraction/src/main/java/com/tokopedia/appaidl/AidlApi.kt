package com.tokopedia.appaidl

import android.app.Application
import android.content.*
import android.os.Bundle
import com.tokopedia.appaidl.data.*
import com.tokopedia.appaidl.ui.ServiceView

open class AidlApi(
        private val application: Application,
        private val listener: ReceiverListener
) {

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == CUSTOMER_APP || intent?.action == SELLER_APP) {
                intent.action?.let {
                    application.unregisterReceiver(this)
                    listener.onAidlReceive(it, intent.extras)
                }
            }
        }
    }

    fun bindAidlService() {
        // the serviceView is serviceConnection to register the receiver in activity and send the data
        val serviceView = ServiceView(aidlTag()) { tag, service ->
            if (service != null) {
                application.registerReceiver(broadcastReceiver, IntentFilter().apply { addAction(tag) })
                service.send(tag)
            }
        }

        val success = application.bindService(Intent().apply {
            component = ComponentName(componentTargetName(), REMOTE_SERVICE)
        }, serviceView, Context.BIND_AUTO_CREATE)

        if (!success) listener.onAidlError()
    }

    interface ReceiverListener {
        fun onAidlReceive(tag: String, bundle: Bundle?)
        fun onAidlError()
    }

}