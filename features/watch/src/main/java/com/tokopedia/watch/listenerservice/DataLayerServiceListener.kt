package com.tokopedia.watch.listenerservice

import android.content.Intent
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import com.tokopedia.watch.TokopediaWatchActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

class DataLayerServiceListener: WearableListenerService() {

    private val messageClient by lazy { Wearable.getMessageClient(this) }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)

        when (messageEvent.path) {
            MESSAGE_CLIENT_START_ORDER_ACTIVITY -> {
                startActivity(
                    Intent(this, TokopediaWatchActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
            MESSAGE_CLIENT_APP_DETECTION -> {
                messageClient.sendMessage(messageEvent.sourceNodeId, MESSAGE_CLIENT_APP_DETECTION, byteArrayOf())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    companion object {
        const val MESSAGE_CLIENT_START_ORDER_ACTIVITY = "/start-order-activity"
        const val MESSAGE_CLIENT_APP_DETECTION = "/app-detection"
        const val GET_ORDER_LIST_PATH = "/get-order-list"
        const val ACCEPT_BULK_ORDER_PATH = "/accept_bulk-order"
    }
}