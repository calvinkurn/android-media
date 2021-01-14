package com.tokopedia.appaidl

import android.content.*
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.appaidl.data.*
import com.tokopedia.appaidl.ui.ServiceView

interface ReceiverListener {
    fun onAidlReceive(tag: String, bundle: Bundle?)
    fun onAidlError()
}

fun BaseActivity.connectService(listener: ReceiverListener) {
    // this broadcastReceiver for receiving the intent data from RemoteService
    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == CUSTOMER_APP || intent?.action == SELLER_APP) {
                unregisterReceiver(this)

                intent.action?.let {
                    listener.onAidlReceive(it, intent.extras)
                }
            }
        }
    }

    // the serviceView is serviceConnection to register the receiver in activity and send the data
    val serviceView = ServiceView(aidlTag()) { tag, service ->
        if (service != null) {
            registerReceiver(broadcastReceiver, IntentFilter().apply { addAction(tag) })
            service.send(tag)
        }
    }

    val success = bindService(Intent().apply {
        component = ComponentName(componentTargetName(), REMOTE_SERVICE)
    }, serviceView, Context.BIND_AUTO_CREATE)

    if (!success) listener.onAidlError()
}