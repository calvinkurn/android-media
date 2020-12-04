package com.tokopedia.notifications.utils

import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import androidx.core.app.NotificationManagerCompat
import com.tokopedia.notifications.model.BaseNotificationModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import com.tokopedia.notifications.database.pushRuleEngine.PushRepository.Companion.getInstance as pushRepository

open class NotificationRemoveManager(
        private val context: Context
): CoroutineScope {

    private val jobs = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + jobs

    fun clearNotification(context: Context) {
        clearNotificationByCampaignId { elements ->
            elements.forEach {
                clearNotificationManager(context, it.notificationId)
            }
        }
    }

    private fun clearNotificationByCampaignId(invoke: (List<BaseNotificationModel>) -> Unit) {
        launch {
            val notifications = pushRepository(context).getNotification()
            withContext(Dispatchers.Main) {
                val result = notifications
                        .intersect(excludeListByCampaignId) { notification, excludedItem ->
                            notification.campaignId == excludedItem.toLong()
                        }
                invoke(result)
            }
        }
    }

    private fun clearNotificationManager(context: Context, notificationId: Int) {
        (context.getSystemService(NOTIFICATION_SERVICE) as? NotificationManager?)?.let { it.cancel(notificationId) }
        NotificationManagerCompat.from(context).cancel(notificationId)
    }

    fun cancel() {
        jobs.cancelChildren()
    }

    companion object {
        private val excludeListByCampaignId = listOf(
                "-334",
                "321",
                "456"
        )
    }

}