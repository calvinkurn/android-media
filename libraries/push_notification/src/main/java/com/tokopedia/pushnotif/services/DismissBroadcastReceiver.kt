package com.tokopedia.pushnotif.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tokopedia.pushnotif.data.constant.Constant
import com.tokopedia.pushnotif.data.model.ApplinkNotificationModel
import com.tokopedia.pushnotif.data.repository.HistoryRepository
import com.tokopedia.pushnotif.domain.TrackPushNotificationUseCase
import com.tokopedia.pushnotif.util.NotificationTracker
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
        val transactionId = intent.getStringExtra(TrackPushNotificationUseCase.PARAM_TRANSACTION_ID).orEmpty()
        val recipientId = intent.getStringExtra(TrackPushNotificationUseCase.PARAM_RECIPIENT_ID).orEmpty()
        val applinkNotificationModel = ApplinkNotificationModel().apply {
            setTransactionId(transactionId)
            toUserId = recipientId
        }

        NotificationTracker
            .getInstance(context)
            .trackDeliveredNotification(
                applinkNotificationModel,
                TrackPushNotificationUseCase.STATUS_DISMISSED
            )

        launch {
            try {
                if (notificationId == 0) {
                    HistoryRepository.clearAllHistoryNotification(context, notificationType)
                } else {
                    HistoryRepository.clearHistoryNotification(context, notificationType, notificationId)
                }
            } catch (ignored: Exception) {

            }
        }

    }
}
