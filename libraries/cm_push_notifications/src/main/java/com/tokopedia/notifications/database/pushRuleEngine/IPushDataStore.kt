package com.tokopedia.notifications.database.pushRuleEngine

import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.NotificationStatus

interface IPushDataStore {
    suspend fun getPendingNotificationList(currentMillis: Long): List<BaseNotificationModel>?
    suspend fun getNotificationByStatusList(status: NotificationStatus): List<BaseNotificationModel>?
    suspend fun getNotificationById(notificationId: Int): BaseNotificationModel?
    suspend fun updateNotificationStatusById(notificationId: Int, status: NotificationStatus)
    suspend fun insertNotification(baseNotificationModel: BaseNotificationModel)
    suspend fun deleteNotification(olderThanExpiryMillis: Long, status: NotificationStatus)
    suspend fun getNotification(): List<BaseNotificationModel>
}