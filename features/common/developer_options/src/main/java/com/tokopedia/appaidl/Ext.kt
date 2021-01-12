package com.tokopedia.appaidl

import android.content.*
import android.os.Bundle
import android.util.Log
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.appaidl.ui.ServiceView
import com.tokopedia.config.GlobalConfig

private const val SERVICE = "com.tokopedia.appaidl.RemoteService"

private const val MAINAPP = "com.tokopedia.tkpd"
private const val SELLERAPP = "com.tokopedia.sellerapp"

interface ReceiverListener {
    fun handleData(tag: String, bundle: Bundle?)
}

fun BaseActivity.connectService(listener: ReceiverListener) {
    // appName is tag
    val appName = if (GlobalConfig.isSellerApp()) SELLERAPP else MAINAPP

    // this broadcastReceiver for receiving the intent data from RemoteService
    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == MAINAPP || intent?.action == SELLERAPP) {
                Log.d("AppApi", "broadcastReceiver")
                unregisterReceiver(this)
                intent.action?.let {
                    listener.handleData(it, intent.extras)
                }
            }
        }
    }

    // the serviceView is serviceConnection to register the receiver in activity and send the data
    val serviceView = ServiceView(appName) { tag, service ->
        if (service != null) {
            Log.d("AppApi", "onServiceConnected stub on serviceView; send() is executed")
            registerReceiver(broadcastReceiver, IntentFilter().apply { addAction(tag) })
            service.send(tag)
        }
    }

    val intent = Intent().apply { component = ComponentName(appName, SERVICE) }
    val success = bindService(intent, serviceView, Context.BIND_AUTO_CREATE)

    if (!success) {
        Log.d("AppApi", "bindService failed")
    }
}