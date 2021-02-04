package com.tokopedia.utils.aidl

import android.content.*
import android.os.Bundle
import com.tokopedia.utils.aidl.data.CUSTOMER_APP
import com.tokopedia.utils.aidl.data.SELLER_APP
import com.tokopedia.utils.aidl.data.aidlTag
import com.tokopedia.utils.aidl.data.componentTargetName
import com.tokopedia.utils.aidl.service.AidlServiceConnection

open class AidlApi(
        private val context: Context,
        private val onAidlReceive: (tag: String, bundle: Bundle?) -> Unit,
        private val onAidlError: () -> Unit
) {

    private var stubService: AidlServiceConnection? = null

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == CUSTOMER_APP || intent?.action == SELLER_APP) {
                intent.action?.let {
                    this@AidlApi.context.unregisterReceiver(this)
                    onAidlReceive(it, intent.extras)
                }
            }
        }
    }

    fun bindService(aidlTag: String = aidlTag(), serviceName: String) {
        // the serviceView is serviceConnection to register the receiver in activity and send the data
        stubService = AidlServiceConnection(aidlTag) { tag, service ->
            if (service != null) {
                context.registerReceiver(broadcastReceiver, IntentFilter().apply { addAction(tag) })
                service.send(tag)
            }
        }.also {
            val success = context.bindService(Intent().apply {
                component = ComponentName(componentTargetName(), serviceName)
            }, it, Context.BIND_AUTO_CREATE)

            if (!success) onAidlError()
        }
    }

    fun unbindService() {
        stubService?.let { context.unbindService(it) }
    }

}