package com.tokopedia.topchat.chatroom.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationChatServiceReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        try {
            NotificationChatService.enqueueWork(context, intent)
        } catch (ignored: Exception) {}
    }
}