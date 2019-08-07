package com.tokopedia.pushnotif

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * @author ricoharisin .
 */

class DismissBroadcastReceiver : BroadcastReceiver(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    override fun onReceive(context: Context, intent: Intent) {
        val notificationType = intent.getIntExtra(Constant.EXTRA_NOTIFICATION_TYPE, 0)
        val notificationId = intent.getIntExtra(Constant.EXTRA_NOTIFICATION_ID, 0)

        launch {
            try {
                if (notificationId == 0) {
                    HistoryNotification.clearAllHistoryNotification(context, notificationType)
                } else {
                    HistoryNotification.clearHistoryNotification(context, notificationType, notificationId)
                }
            } catch (ignored: Exception) {

            }
        }

    }
}
