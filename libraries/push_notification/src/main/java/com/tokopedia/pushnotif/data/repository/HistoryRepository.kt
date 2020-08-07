package com.tokopedia.pushnotif.data.repository

import android.content.Context
import com.tokopedia.pushnotif.data.db.PushNotificationDB.Companion.getInstance
import com.tokopedia.pushnotif.data.db.model.HistoryNotification

/**
 * @author ricoharisin .
 */
object HistoryRepository {

    private const val HISTORY_NOTIFICATION_LIMIT = 5

    @JvmStatic
    fun storeNotification(
            context: Context,
            senderName: String?,
            message: String?,
            notificationType: Int,
            notificationId: Int
    ) {
        val data = HistoryNotification(
                senderName,
                message,
                notificationType,
                notificationId
        )
        getInstance(context)
                .historyNotificationDao()
                .storeNotification(data)
    }

    @JvmStatic
    fun getListHistoryNotification(
            context: Context,
            notificationType: Int
    ): List<HistoryNotification> {
        return getInstance(context)
                .historyNotificationDao()
                .getListHistoryNotification(notificationType, HISTORY_NOTIFICATION_LIMIT)
    }

    @JvmStatic
    fun clearHistoryNotification(context: Context, notificationType: Int, notificationId: Int) {
        getInstance(context)
                .historyNotificationDao()
                .clearHistoryNotification(notificationType, notificationId)
    }

    @JvmStatic
    fun clearAllHistoryNotification(context: Context, notificationType: Int) {
        getInstance(context)
                .historyNotificationDao()
                .clearAllHistoryNotification(notificationType)
    }

    @JvmStatic
    fun isSingleNotification(context: Context, notificationType: Int): Boolean {
        return getInstance(context)
                .historyNotificationDao()
                .countNotification(notificationType) == 0
    }

}