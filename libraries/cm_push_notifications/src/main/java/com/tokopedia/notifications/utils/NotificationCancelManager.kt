package com.tokopedia.notifications.utils

import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import androidx.core.app.NotificationManagerCompat
import com.tokopedia.notifications.common.CMRemoteConfigUtils
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.remoteconfig.RemoteConfigKey.CM_CAMPAIGN_ID_EXCLUDE_LIST
import com.tokopedia.remoteconfig.RemoteConfigKey.NOTIFICATION_TRAY_CLEAR
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import com.tokopedia.notifications.database.pushRuleEngine.PushRepository.Companion.getInstance as pushRepository

open class NotificationCancelManager(
        private val context: Context
): CoroutineScope {

    private val jobs = SupervisorJob()
    private val remoteConfig by lazy {
        CMRemoteConfigUtils(context)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + jobs

    fun clearNotifications() {
        if (remoteConfig.getBooleanRemoteConfig(NOTIFICATION_TRAY_CLEAR, false)) {
            getCancellableNotifications(context) { notifications ->
                notifications.forEach {
                    cancelNotificationManager(context, it.notificationId)
                }
            }
        }
    }

    fun cancel() {
        jobs.cancelChildren()
    }

    private fun getCancellableNotifications(
            context: Context,
            invoke: (List<BaseNotificationModel>) -> Unit
    ) {
        launch {
            val notifications = pushRepository(context).getNotification()
            withContext(Dispatchers.Main) {
                val result = notifications
                        .intersect(excludeCampaignIdList()) { notification, excludedItem ->
                            notification.campaignId == excludedItem.toLong()
                        }
                invoke(result)
            }
        }
    }

    private fun excludeCampaignIdList(): List<String> {
        val campaignIds = remoteConfig.getStringRemoteConfig(CM_CAMPAIGN_ID_EXCLUDE_LIST)
        return campaignIds.split(",").map { it.trim() }
    }

    private fun cancelNotificationManager(context: Context, notificationId: Int) {
        NotificationManagerCompat.from(context).cancel(notificationId)
        (context.getSystemService(NOTIFICATION_SERVICE) as? NotificationManager?)?.let {
            it.cancel(notificationId)
        }
    }

}