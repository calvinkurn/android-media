package com.tokopedia.tokochat.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class TokoChatPushNotifBroadcastReceiver(
    private val listener: TokoChatPushNotifBroadcastListener
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_NOTIFICATION_CLICK) {
            listener.onPushNotifClick(intent)
        }
    }

    companion object {
        const val ACTION_NOTIFICATION_CLICK = "com.tokopedia.notification.ACTION_NOTIFICATION_CLICK"
    }

    interface TokoChatPushNotifBroadcastListener {
        fun onPushNotifClick(intent: Intent)
    }
}
