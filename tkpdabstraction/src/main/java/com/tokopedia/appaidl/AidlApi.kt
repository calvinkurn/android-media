package com.tokopedia.appaidl

import android.content.*
import android.os.Bundle
import com.tokopedia.appaidl.data.*
import com.tokopedia.appaidl.ui.ServiceView

open class AidlApi(private val context: Context, private val listener: ReceiverListener) {

    private var serviceView: ServiceView? = null

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

    fun bindService() {
        // the serviceView is serviceConnection to register the receiver in activity and send the data
        serviceView = ServiceView(aidlTag()) { tag, service ->
            if (service != null) {
                context.registerReceiver(broadcastReceiver, IntentFilter().apply { addAction(tag) })
                service.send(tag)
            }
        }.also {
            val success = context.bindService(Intent().apply {
                component = ComponentName(componentTargetName(), REMOTE_SERVICE)
            }, it, Context.BIND_AUTO_CREATE)

            if (!success) listener.onAidlError()
        }
    }

    fun unbindService() {
        serviceView?.let { context.unbindService(it) }
    }

    interface ReceiverListener {
        fun onAidlReceive(tag: String, bundle: Bundle?)
        fun onAidlError()
    }

}