package com.tokopedia.notifications.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * @author lalit.singh
 */

class CMBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        CMNotificationHandler.instance.handleIntent(context, intent)
    }
}