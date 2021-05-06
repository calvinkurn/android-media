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

    fun cancel() = jobs.cancelChildren()

    fun isCancellable(activity: Activity): Boolean {
        return !TARGET_ACTIVITIES.singleOrNull {
            it == activity.javaClass.canonicalName
        }.isNullOrEmpty()
    }

    fun clearNotifications(context: Context) {
        val remoteConfig = CMRemoteConfigUtils(context)

        if (remoteConfig.getBooleanRemoteConfig(NOTIFICATION_TRAY_CLEAR, false)) {
            cancellableItems(context, remoteConfig) { notifications ->
                kotlin.runCatching {
                    notifications.forEach {
                        cancelNotificationManager(context, it.notificationId)
                    }
                }
            }
        }
    }

    private fun cancellableItems(
            context: Context,
            remoteConfig: CMRemoteConfigUtils,
            invoke: (List<BaseNotificationModel>) -> Unit
    ) {
        launch {
            val notifications = pushRepository(context).getNotification()
            withContext(Dispatchers.Main) {
                runCatching {
                    invoke(notifications
                            .filter { it.campaignId != 0L }
                            .intersect(excludeIds(remoteConfig)) { notification, excludedItem ->
                                notification.campaignId == excludedItem.toLong()
                            }
                    )
                }
            }
        }
    }

    private fun excludeIds(remoteConfig: CMRemoteConfigUtils): List<String> {
        val campaignIds = remoteConfig.getStringRemoteConfig(CM_CAMPAIGN_ID_EXCLUDE_LIST)
        return if (campaignIds.isNotEmpty()) {
            campaignIds.split(",").map { it.trim() }
        } else {
            excludeIds
        }
    }

    private fun cancelNotificationManager(context: Context, notificationId: Int) {
        NotificationManagerCompat.from(context).cancel(notificationId)
        (context.getSystemService(NOTIFICATION_SERVICE) as? NotificationManager?)?.cancel(notificationId)
    }

    companion object {
        /*
        * The NotificationCancelManager only called in the specific current activity,
        * to preventing multiple called in another activity opened we need to make a
        * list of TARGET_ACTIVITIES to specify of main activity.
        * */
        val TARGET_ACTIVITIES = listOf(
                "com.tokopedia.navigation.presentation.activity.MainParentActivity",
                "com.tokopedia.sellerhome.view.activity.SellerHomeActivity"
        )

        /*
        * NotificationCancelManager will trigger every main activity opened,
        * so there's case where remote config value didn't get the value well,
        * so we need a internal exclude static items of campaignId.
        * */
        private val excludeIds = listOf(
                "-1854" // OTP Push Notification
        )
    }

}