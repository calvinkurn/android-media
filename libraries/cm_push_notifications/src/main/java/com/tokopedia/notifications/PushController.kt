package com.tokopedia.notifications

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.tokopedia.notifications.common.*
import com.tokopedia.notifications.data.converters.JsonBundleConverter.jsonToBundle
import com.tokopedia.notifications.database.pushRuleEngine.PushRepository
import com.tokopedia.notifications.factory.CMNotificationFactory
import com.tokopedia.notifications.image.ImageDownloadManager
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.NotificationMode
import com.tokopedia.notifications.model.NotificationStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class PushController(val context: Context) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private val cmRemoteConfigUtils by lazy { CMRemoteConfigUtils(context) }

    private val isOfflinePushEnabled
        get() = cmRemoteConfigUtils.getBooleanRemoteConfig(CMConstant.RemoteKeys.KEY_IS_OFFLINE_PUSH_ENABLE, false)

    fun handleNotificationBundle(bundle: Bundle, isAmplification: Boolean = false) {
        try {
            //todo event notification received offline
            launchCatchError(
                    block = {
                        Log.d("PUSHController", "Code update")

                        val baseNotificationModel = PayloadConverter.convertToBaseModel(bundle)
                        if (isAmplification) baseNotificationModel.isAmplification = true
                        if (baseNotificationModel.notificationMode == NotificationMode.OFFLINE) {
                            if (isOfflinePushEnabled)
                                onOfflinePushPayloadReceived(baseNotificationModel)
                        } else {
                            onLivePushPayloadReceived(baseNotificationModel)
                        }
                    }, onError = {
                Timber.w( "${CMConstant.TimberTags.TAG}exception;err='${Log.getStackTraceString(it)
                        .take(CMConstant.TimberTags.MAX_LIMIT)}';data='${bundle.toString()
                        .take(CMConstant.TimberTags.MAX_LIMIT)}'")
            })

        } catch (e: Exception) {
            Timber.w( "${CMConstant.TimberTags.TAG}exception;err='${Log.getStackTraceString(e)
                    .take(CMConstant.TimberTags.MAX_LIMIT)}';data='${bundle.toString()
                    .take(CMConstant.TimberTags.MAX_LIMIT)}'")
        }
    }

    fun handleNotificationAmplification(payloadJson: String) {
        try {
            launchCatchError(block = {
                val model = Gson().fromJson(
                        payloadJson,
                        BaseNotificationModel::class.java
                )
                if (!isOfflineNotificationActive(model.notificationId)) {
                    val bundle = jsonToBundle(payloadJson)
                    handleNotificationBundle(bundle, true)
                }
            }, onError = {})
        } catch (e: Exception) { }
    }

    private suspend fun onLivePushPayloadReceived(baseNotificationModel: BaseNotificationModel) {

        var updatedBaseNotificationModel: BaseNotificationModel? = null
        if (baseNotificationModel.type == CMConstant.NotificationType.DELETE_NOTIFICATION) {
            baseNotificationModel.status = NotificationStatus.COMPLETED
            createAndPostNotification(baseNotificationModel)
        }
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
            Timber.w( "${CMConstant.TimberTags.TAG}exception;err='${Log.getStackTraceString(e)
                    .take(CMConstant.TimberTags.MAX_LIMIT)}';data='${baseNotificationModel.toString()
                    .take(CMConstant.TimberTags.MAX_LIMIT)}'")
        }
    }

}