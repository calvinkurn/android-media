package com.tokopedia.notifications.database.pushRuleEngine

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.tokopedia.notifications.common.StringDecryptTypeAdapter
import com.tokopedia.notifications.common.StringEncryptTypeAdapter
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.NotificationStatus

class PushDataStore(private val baseNotificationDao: BaseNotificationDao) : IPushDataStore {
    override suspend fun getNotificationById(notificationId: Int): BaseNotificationModel? {
        val baseNotificationModel = baseNotificationDao.getNotificationById(notificationId)
        val modelToString = Gson().toJson(baseNotificationModel)
        val gson = GsonBuilder().registerTypeAdapter(String::class.java, StringDecryptTypeAdapter.instance()).create()
        return gson.fromJson(modelToString, BaseNotificationModel::class.java)
    }

    override suspend fun deleteNotification(olderThanExpiryMillis: Long, status: NotificationStatus) {
        return baseNotificationDao.deleteNotification(olderThanExpiryMillis, status)
    }

    override suspend fun insertNotification(baseNotificationModel: BaseNotificationModel) {
        val modelToString = Gson().toJson(baseNotificationModel)
        val gson = GsonBuilder().registerTypeAdapter(String::class.java, StringEncryptTypeAdapter.instance()).create()
        val encryptedModel = gson.fromJson(modelToString, BaseNotificationModel::class.java)

        return baseNotificationDao.insertNotificationModel(encryptedModel)
    }

    override suspend fun getPendingNotificationList(currentMillis: Long): List<BaseNotificationModel> {
        return getDecryptedList(baseNotificationDao.getPendingNotification(currentMillis))
    }

    private fun getDecryptedList(notificationList: List<BaseNotificationModel>): List<BaseNotificationModel> {
        val listToString = Gson().toJson(notificationList)
        val gson = GsonBuilder().registerTypeAdapter(String::class.java, StringDecryptTypeAdapter.instance()).create()
        return gson.fromJson(listToString, object : TypeToken<List<BaseNotificationModel>>() {}.type)
    }

    override suspend fun getNotificationByStatusList(status: NotificationStatus): List<BaseNotificationModel> {
        return getDecryptedList(baseNotificationDao.getNotificationByStatusList(status))
    }

    override suspend fun updateNotificationStatusById(notificationId: Int, status: NotificationStatus) {
        baseNotificationDao.updateNotificationStatus(notificationId, status)
    }

}