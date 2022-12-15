package com.tokopedia.pushnotif.data.repository

import android.content.Context
import com.tokopedia.pushnotif.data.constant.Constant
import com.tokopedia.pushnotif.data.db.PushNotificationDB.Companion.getInstance
import com.tokopedia.pushnotif.data.db.model.HistoryNotification
import com.tokopedia.pushnotif.data.model.ApplinkNotificationModel

/**
 * @author ricoharisin .
 */
object HistoryRepository {

    private const val HISTORY_NOTIFICATION_LIMIT = 5

    @JvmStatic
    fun storeNotification(
        context: Context,
        applinkNotificationModel: ApplinkNotificationModel,
        notificationType: Int,
        notificationId: Int
    ) {
        val thumbnailUrl = if (applinkNotificationModel.thumbnail.isNullOrBlank()) {
            Constant.DEFAULT_AVATAR_URL
        } else {
            applinkNotificationModel.thumbnail
        }
        val data = HistoryNotification(
            applinkNotificationModel.fullName,
            applinkNotificationModel.summary,
            notificationType,
            notificationId,
            thumbnailUrl,
            applinkNotificationModel.applinks,
        )
        runCatching {
            getInstance(context)
                .historyNotificationDao()
                .storeNotification(data)
        }
    }

    @JvmStatic
    fun getListHistoryNotification(
            context: Context,
            notificationType: Int
    ): List<HistoryNotification> {
        return runCatching {
            getInstance(context)
                .historyNotificationDao()
                .getListHistoryNotification(notificationType, HISTORY_NOTIFICATION_LIMIT)
        }.getOrDefault(arrayListOf())

    }

    @JvmStatic
    fun clearHistoryNotification(context: Context, notificationType: Int, notificationId: Int) {
        runCatching {
            getInstance(context)
                .historyNotificationDao()
                .clearHistoryNotification(notificationType, notificationId)
        }

    }

    @JvmStatic
    fun clearAllHistoryNotification(context: Context, notificationType: Int) {
        runCatching {
            getInstance(context)
                .historyNotificationDao()
                .clearAllHistoryNotification(notificationType)
        }
    }

    @JvmStatic
    fun isSingleNotification(context: Context, notificationType: Int): Boolean {
        return runCatching {
            getInstance(context)
                .historyNotificationDao()
                .countNotification(notificationType) == 0
        }.getOrDefault(true)
    }

}
