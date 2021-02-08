package com.tokopedia.notifications.utils

import android.app.Activity
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

open class NotificationCancelManager: CoroutineScope {

    private val jobs = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + jobs

    fun isCancellable(activity: Activity): Boolean {
        return !TARGET_ACTIVITIES.singleOrNull {
            it == activity.javaClass.canonicalName
        }.isNullOrEmpty()
    }

    fun clearNotifications(context: Context) {
        val remoteConfig = CMRemoteConfigUtils(context)

        if (remoteConfig.getBooleanRemoteConfig(NOTIFICATION_TRAY_CLEAR, false)) {
            cancellableItems(context, remoteConfig) { notifications ->
                notifications.forEach {
                    cancelNotificationManager(context, it.notificationId)
                }
            }
        }
    }

    fun cancel() {
        jobs.cancelChildren()
    }

    private fun cancellableItems(
            context: Context,
            remoteConfig: CMRemoteConfigUtils,
            invoke: (List<BaseNotificationModel>) -> Unit
    ) {
        launch {
            val notifications = pushRepository(context).getNotification()
            withContext(Dispatchers.Main) {
                val result = notifications
                        .intersect(excludeIds(remoteConfig)) { notification, excludedItem ->
                            notification.campaignId == excludedItem.toLong()
                        }
                invoke(result)
            }
        }
    }

    private fun excludeIds(remoteConfig: CMRemoteConfigUtils): List<String> {
        val campaignIds = remoteConfig.getStringRemoteConfig(CM_CAMPAIGN_ID_EXCLUDE_LIST)
        return campaignIds.split(",").map { it.trim() }
    }

    private fun cancelNotificationManager(context: Context, notificationId: Int) {
        NotificationManagerCompat.from(context).cancel(notificationId)
        (context.getSystemService(NOTIFICATION_SERVICE) as? NotificationManager?)?.let {
            it.cancel(notificationId)
        }
    }

    companion object {
        /*
        * TARGET_ACTIVITIES;
        * the NotificationCancelManager only called in the specific current activity
        * to preventing multiple called every another activity opened.
        * */
        val TARGET_ACTIVITIES = listOf(
                "com.tokopedia.navigation.presentation.activity.MainParentActivity",
                "com.tokopedia.sellerhome.view.activity.SellerHomeActivity"
        )
    }

}