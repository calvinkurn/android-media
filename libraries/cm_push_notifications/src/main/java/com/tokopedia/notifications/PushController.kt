package com.tokopedia.notifications

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.PayloadConverter
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.notifications.database.pushRuleEngine.PushRepository
import com.tokopedia.notifications.factory.CMNotificationFactory
import com.tokopedia.notifications.image.ImageDownloadManager
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.NotificationMode
import com.tokopedia.notifications.model.NotificationStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class PushController(val context: Context) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    fun handleNotificationBundle(bundle: Bundle) {
        try {
            //todo event notification received offline
            launchCatchError(
                    block = {
                        Log.d("PUSHController", "Code update")

                        val baseNotificationModel = PayloadConverter.convertToBaseModel(bundle)
                        if (baseNotificationModel.notificationMode == NotificationMode.OFFLINE) {
                            onOfflinePushPayloadReceived(baseNotificationModel)
                        } else {
                            onLivePushPayloadReceived(baseNotificationModel)
                        }
                    }, onError = {
                Log.d("PUSHController", it.message)
            })

        } catch (e: Exception) {
        }
    }

    private suspend fun onLivePushPayloadReceived(baseNotificationModel: BaseNotificationModel) {

        var updatedBaseNotificationModel: BaseNotificationModel? = null
        if (baseNotificationModel.type == CMConstant.NotificationType.DELETE_NOTIFICATION)
            baseNotificationModel.status = NotificationStatus.COMPLETED
        else if (baseNotificationModel.startTime == 0L
                || baseNotificationModel.endTime > System.currentTimeMillis()) {

            updatedBaseNotificationModel = ImageDownloadManager.downloadImages(context, baseNotificationModel)
            updatedBaseNotificationModel?.let {
                createAndPostNotification(updatedBaseNotificationModel)
            } ?: createAndPostNotification(baseNotificationModel)

            baseNotificationModel.status = NotificationStatus.ACTIVE
        } else {
            baseNotificationModel.status = NotificationStatus.COMPLETED
        }
        updatedBaseNotificationModel?.let {
            PushRepository.getInstance(context)
                    .insertNotificationModel(updatedBaseNotificationModel)
        } ?: PushRepository.getInstance(context)
                .insertNotificationModel(baseNotificationModel)
    }

    private suspend fun onOfflinePushPayloadReceived(baseNotificationModel: BaseNotificationModel) {
        if (baseNotificationModel.type == CMConstant.NotificationType.DELETE_NOTIFICATION) {
            baseNotificationModel.status = NotificationStatus.DELETE
            if (isOfflineNotificationActive(baseNotificationModel.notificationId)) {
                cancelOfflineNotification(baseNotificationModel)
                baseNotificationModel.status = NotificationStatus.COMPLETED
                PushRepository.getInstance(context).pushDataStore.insertNotification(baseNotificationModel)
            }
        } else {
            baseNotificationModel.status = NotificationStatus.PENDING
            val updatedBaseNotificationModel = ImageDownloadManager.downloadImages(context, baseNotificationModel)
            updatedBaseNotificationModel?.let {
                PushRepository.getInstance(context).insertNotificationModel(updatedBaseNotificationModel)
            } ?: PushRepository.getInstance(context).insertNotificationModel(baseNotificationModel)
        }
    }

    private suspend fun isOfflineNotificationActive(notificationID: Int): Boolean {
        val baseNotificationModel = PushRepository.getInstance(context)
                .pushDataStore.getNotificationById(notificationID)
        baseNotificationModel?.let {
            return baseNotificationModel.status == NotificationStatus.ACTIVE
        } ?: return false
    }

    suspend fun postOfflineNotification(baseNotificationModel: BaseNotificationModel) {
        createAndPostNotification(baseNotificationModel)
        baseNotificationModel.status = NotificationStatus.ACTIVE
        PushRepository.getInstance(context).updateNotificationModel(baseNotificationModel)
    }

    suspend fun cancelOfflineNotification(baseNotificationModel: BaseNotificationModel) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(baseNotificationModel.notificationId)
        baseNotificationModel.status = NotificationStatus.COMPLETED
        PushRepository.getInstance(context).updateNotificationModel(baseNotificationModel)
    }

    private fun createAndPostNotification(baseNotificationModel: BaseNotificationModel) {
        try {
            val baseNotification = CMNotificationFactory
                    .getNotification(context.applicationContext, baseNotificationModel)
            if (null != baseNotification) {
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val notification = baseNotification.createNotification()
                notificationManager.notify(baseNotification.baseNotificationModel.notificationId, notification)
            }
        } catch (e: Exception) {
        }
    }

}