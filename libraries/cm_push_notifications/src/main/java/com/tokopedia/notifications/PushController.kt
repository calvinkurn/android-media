package com.tokopedia.notifications

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.google.gson.*
import com.tokopedia.notifications.common.*
import com.tokopedia.notifications.database.pushRuleEngine.PushRepository
import com.tokopedia.notifications.factory.CMNotificationFactory
import com.tokopedia.notifications.image.ImageDownloadManager
import com.tokopedia.notifications.model.AmplificationBaseNotificationModel
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.NotificationMode
import com.tokopedia.notifications.model.NotificationStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class PushController(val context: Context) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private val cmRemoteConfigUtils by lazy { CMRemoteConfigUtils(context) }

    private val isOfflinePushEnabled
        get() = cmRemoteConfigUtils.getBooleanRemoteConfig(CMConstant.RemoteKeys.KEY_IS_OFFLINE_PUSH_ENABLE, false)

    fun handleNotificationBundle(bundle: Bundle, isAmplification: Boolean = false) {
        try {
            val baseNotificationModel = PayloadConverter.convertToBaseModel(bundle)
            handleNotificationBundle(baseNotificationModel, isAmplification)
        } catch (e: Exception) {
            ServerLogger.log(Priority.P2, "CM_VALIDATION",
                    mapOf("type" to "exception",
                            "err" to Log.getStackTraceString(e).take(CMConstant.TimberTags.MAX_LIMIT),
                            "data" to bundle.toString().take(CMConstant.TimberTags.MAX_LIMIT)))
        }
    }

    private fun handleNotificationBundle(baseNotificationModel: BaseNotificationModel, isAmplification: Boolean = false) {
        launchCatchError(
                block = {
                    if (isAmplification) baseNotificationModel.isAmplification = true
                    if (baseNotificationModel.notificationMode == NotificationMode.OFFLINE) {
                        if (isOfflinePushEnabled)
                            onOfflinePushPayloadReceived(baseNotificationModel)
                    } else {
                        onLivePushPayloadReceived(baseNotificationModel)
                    }
                }, onError = {
            ServerLogger.log(Priority.P2, "CM_VALIDATION",
                    mapOf("type" to "exception",
                            "err" to Log.getStackTraceString(it).take(CMConstant.TimberTags.MAX_LIMIT),
                            "data" to baseNotificationModel.toString().take(CMConstant.TimberTags.MAX_LIMIT)))
        })
    }

    fun handleNotificationAmplification(payloadJson: String) {
        try {
            launchCatchError(block = {
                val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
                val amplificationBaseNotificationModel = gson.fromJson(payloadJson, AmplificationBaseNotificationModel::class.java)
                val model = PayloadConverter.convertToBaseModel(amplificationBaseNotificationModel)
                if (isAmpNotificationValid(model.notificationId)) {
                    handleNotificationBundle(model, true)
                }
            }, onError = {
                println(it.message)
                ServerLogger.log(Priority.P2, "CM_VALIDATION",
                        mapOf("type" to "exception",
                                "err" to Log.getStackTraceString(it).take(CMConstant.TimberTags.MAX_LIMIT),
                                "data" to ""))
            })
        } catch (e: Exception) {
            ServerLogger.log(Priority.P2, "CM_VALIDATION",
                    mapOf("type" to "exception",
                            "err" to Log.getStackTraceString(e).take(CMConstant.TimberTags.MAX_LIMIT),
                            "data" to ""))
        }
    }

    private suspend fun isAmpNotificationValid(notificationID: Int): Boolean {
        val baseNotificationModel = PushRepository.getInstance(context)
                .pushDataStore.getNotificationById(notificationID)

        return baseNotificationModel == null
    }

    fun cancelPushNotification(bundle: Bundle) {
        PayloadConverter.convertToBaseModel(bundle).apply {
            type = CMConstant.NotificationType.DROP_NOTIFICATION
        }.also {
            handleNotificationBundle(it)
        }
    }

    private suspend fun onLivePushPayloadReceived(baseNotificationModel: BaseNotificationModel) {

        var updatedBaseNotificationModel: BaseNotificationModel? = null
        if (baseNotificationModel.type == CMConstant.NotificationType.DELETE_NOTIFICATION) {
            baseNotificationModel.status = NotificationStatus.COMPLETED
            createAndPostNotification(baseNotificationModel)
        } else if (baseNotificationModel.startTime == 0L
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
            IrisAnalyticsEvents.sendPushEvent(context, IrisAnalyticsEvents.PUSH_DELETED, baseNotificationModel)
            if (isOfflineNotificationActive(baseNotificationModel.notificationId)) {
                cancelOfflineNotification(baseNotificationModel)
                baseNotificationModel.status = NotificationStatus.COMPLETED
            }
            PushRepository.getInstance(context).pushDataStore.insertNotification(baseNotificationModel)

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
        IrisAnalyticsEvents.sendPushEvent(context, IrisAnalyticsEvents.PUSH_RECEIVED, baseNotificationModel)
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
            ServerLogger.log(Priority.P2, "CM_VALIDATION",
                    mapOf("type" to "exception",
                            "err" to Log.getStackTraceString(e).take(CMConstant.TimberTags.MAX_LIMIT),
                            "data" to baseNotificationModel.toString().take(CMConstant.TimberTags.MAX_LIMIT)))
        }
    }

}