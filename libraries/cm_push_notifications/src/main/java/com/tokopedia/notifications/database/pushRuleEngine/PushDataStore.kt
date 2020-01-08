package com.tokopedia.notifications.database.pushRuleEngine

import com.tokopedia.notifications.common.CMPushEncoderDecoder
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.NotificationStatus

class PushDataStore(private val baseNotificationDao: BaseNotificationDao) : IPushDataStore {
    override suspend fun getNotificationById(notificationId: Int): BaseNotificationModel? {
        val baseNotificationModel = baseNotificationDao.getNotificationById(notificationId)
        CMPushEncoderDecoder.decodeBaseNotificationModel(baseNotificationModel)
        return baseNotificationModel
    }

    override suspend fun deleteNotification(olderThanExpiryMillis: Long, status: NotificationStatus) {
        return baseNotificationDao.deleteNotification(olderThanExpiryMillis, status)
    }

    override suspend fun insertNotification(baseNotificationModel: BaseNotificationModel) {
        CMPushEncoderDecoder.encodeBaseNotificationModel(baseNotificationModel)
        return baseNotificationDao.insertNotificationModel(baseNotificationModel)
    }

    override suspend fun getPendingNotificationList(currentMillis: Long): List<BaseNotificationModel> {
        return getDecryptedList(baseNotificationDao.getPendingNotification(currentMillis))
    }

    private fun getDecryptedList(notificationList: List<BaseNotificationModel>): List<BaseNotificationModel> {
        notificationList.forEach { CMPushEncoderDecoder.decodeBaseNotificationModel(it) }
        return notificationList
    }

    override suspend fun getNotificationByStatusList(status: NotificationStatus): List<BaseNotificationModel> {
        return getDecryptedList(baseNotificationDao.getNotificationByStatusList(status))
    }

    override suspend fun updateNotificationStatusById(notificationId: Int, status: NotificationStatus) {
        baseNotificationDao.updateNotificationStatus(notificationId, status)
    }

}