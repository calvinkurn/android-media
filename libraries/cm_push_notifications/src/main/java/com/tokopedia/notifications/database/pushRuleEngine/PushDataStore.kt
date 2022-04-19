package com.tokopedia.notifications.database.pushRuleEngine

import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.NotificationStatus

class PushDataStore(private val baseNotificationDao: BaseNotificationDao) : IPushDataStore {

    override suspend fun getNotificationById(notificationId: Int): BaseNotificationModel? {
        return baseNotificationDao.getNotificationById(notificationId)
    }

    override suspend fun deleteNotification(olderThanExpiryMillis: Long, status: NotificationStatus) {
        return baseNotificationDao.deleteNotification(olderThanExpiryMillis, status)
    }

    override suspend fun insertNotification(baseNotificationModel: BaseNotificationModel) {
        return baseNotificationDao.insertNotificationModel(baseNotificationModel)
    }

    override suspend fun getPendingNotificationList(currentMillis: Long): List<BaseNotificationModel> {
        return baseNotificationDao.getPendingNotification(currentMillis)
    }

    override suspend fun getNotificationByStatusList(status: NotificationStatus): List<BaseNotificationModel> {
        return baseNotificationDao.getNotificationByStatusList(status)
    }

    override suspend fun updateNotificationStatusById(notificationId: Int, status: NotificationStatus) {
        baseNotificationDao.updateNotificationStatus(notificationId, status)
    }

    override suspend fun getNotification(): List<BaseNotificationModel> {
        return try {
            baseNotificationDao.getNotification()
        } catch (e: Exception) {
            arrayListOf()
        }
    }

}