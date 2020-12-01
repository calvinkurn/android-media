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
        notificationIdToClear { elements ->
            elements.forEach {
                clearNotificationManager(context, it.notificationId)
            }
        }
    }

    fun cancel() {
        jobs.cancelChildren()
    }

    private fun notificationIdToClear(invoke: (List<BaseNotificationModel>) -> Unit) {
        launch {
            val notifications = pushRepository(context)
                    .getNotification()
                    .intersect(excludeListByCampaignId) { notification, excludedItem ->
                        notification.campaignId != excludedItem
                    }
            withContext(Dispatchers.Main) { invoke(notifications) }
        }
    }

    private fun clearNotificationManager(context: Context, notificationId: Int) {
        (context.getSystemService(NOTIFICATION_SERVICE) as? NotificationManager?)
                ?.let {
                    it.cancel(notificationId)
                }

        NotificationManagerCompat
                .from(context)
                .cancel(notificationId)
    }

    companion object {
        private val excludeListByCampaignId = listOf(0L)
    }

}