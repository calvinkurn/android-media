package com.tokopedia.notifications.push

import android.app.KeyguardManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.notifications.common.*
import com.tokopedia.notifications.database.pushRuleEngine.PushRepository
import com.tokopedia.notifications.factory.CMNotificationFactory
import com.tokopedia.notifications.image.ImageDownloadManager
import com.tokopedia.notifications.model.AdvanceTargetingData
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.NotificationMode
import com.tokopedia.notifications.model.NotificationStatus
import com.tokopedia.notifications.utils.NotificationSettingsUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext


class PushController(val context: Context) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private val cmRemoteConfigUtils by lazy { CMRemoteConfigUtils(context) }

    private val isOfflinePushEnabled
        get() = cmRemoteConfigUtils.getBooleanRemoteConfig(CMConstant.RemoteKeys.KEY_IS_OFFLINE_PUSH_ENABLE, false)

    private val isAutoRedirect
        get() = cmRemoteConfigUtils.getBooleanRemoteConfig(AUTO_REDIRECTION_REMOTE_CONFIG_KEY, false)


    fun handleProcessedPushPayload(aidlApiBundle : Bundle?,
                                   baseNotificationModel: BaseNotificationModel,
                                   advanceTargetingData: AdvanceTargetingData){
        postEventForLiveNotification(baseNotificationModel)
        NotificationAdvanceTargetingHandler().checkForValidityAndAdvanceTargeting(
            context.applicationContext,
            aidlApiBundle,
            baseNotificationModel,
            advanceTargetingData, onValid = {
                handleNotificationBundle(baseNotificationModel)
            }, onCancel = {
                cancelPushNotification(baseNotificationModel)
            })
    }

    private fun handleNotificationBundle(model: BaseNotificationModel) {
        launchCatchError(
                block = {
                    if(model.isAmplification){
                        if(!isAmpNotificationValid(model.notificationId))
                            return@launchCatchError
                    }
                    if (model.notificationMode == NotificationMode.OFFLINE) {
                        if (isOfflinePushEnabled)
                            onOfflinePushPayloadReceived(model)
                    } else {
                        onLivePushPayloadReceived(model)
                    }
                }, onError = {
            ServerLogger.log(Priority.P2, "CM_VALIDATION",
                    mapOf("type" to "exception",
                            "err" to Log.getStackTraceString(it).take(CMConstant.TimberTags.MAX_LIMIT),
                            "data" to model.toString().take(CMConstant.TimberTags.MAX_LIMIT)))
        })
    }

    private suspend fun isAmpNotificationValid(notificationID: Int): Boolean {
        val baseNotificationModel = PushRepository.getInstance(context)
                .pushDataStore.getNotificationById(notificationID)

        return baseNotificationModel == null
    }

    private fun cancelPushNotification(baseNotificationModel: BaseNotificationModel) {
        baseNotificationModel.apply {
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
        checkNotificationChannelAndSendEvent(baseNotificationModel)
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
            if (checkOtpPushNotif(baseNotificationModel.appLink)) {
                goToOtpPushNotifReceiver(baseNotificationModel.appLink)
            } else if (null != baseNotification) {
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

    private fun checkOtpPushNotif(applink: String?): Boolean {
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return if (Build.VERSION.SDK_INT < ANDROID_12_SDK_VERSION &&
                !keyguardManager.isKeyguardLocked &&
            isAutoRedirect
        ) {
            applink?.startsWith(ApplinkConst.OTP_PUSH_NOTIF_RECEIVER) == true
        } else {
            false
        }
    }

    private fun goToOtpPushNotifReceiver(applink: String?) {
        val intentHome = RouteManager.getIntent(context, ApplinkConst.HOME)
        val intent = RouteManager.getIntent(context, applink)
        intentHome.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivities(arrayOf(intentHome, intent))
    }

    private fun postEventForLiveNotification(baseNotificationModel: BaseNotificationModel){
        if (baseNotificationModel.notificationMode == NotificationMode.OFFLINE) return
        if (baseNotificationModel.type == CMConstant.NotificationType.SILENT_PUSH) {
            IrisAnalyticsEvents.sendPushEvent(
                context,
                IrisAnalyticsEvents.PUSH_RECEIVED,
                baseNotificationModel
            )
        } else {
            checkNotificationChannelAndSendEvent(baseNotificationModel);
        }
    }

    private fun checkNotificationChannelAndSendEvent(baseNotificationModel: BaseNotificationModel){
        when (NotificationSettingsUtils(context).checkNotificationsModeForSpecificChannel(
            baseNotificationModel.channelName
        )) {
            NotificationSettingsUtils.NotificationMode.ENABLED -> {
                IrisAnalyticsEvents.sendPushEvent(
                    context,
                    IrisAnalyticsEvents.PUSH_RECEIVED,
                    baseNotificationModel
                )
            }
            NotificationSettingsUtils.NotificationMode.DISABLED,
            NotificationSettingsUtils.NotificationMode.CHANNEL_DISABLED -> {
                IrisAnalyticsEvents.sendPushEvent(
                    context,
                    IrisAnalyticsEvents.DEVICE_NOTIFICATION_OFF,
                    baseNotificationModel
                )
            }
        }
    }

    companion object {
        const val ANDROID_12_SDK_VERSION = 31
        const val AUTO_REDIRECTION_REMOTE_CONFIG_KEY = "android_user_otp_push_notif_auto_redirection"
    }
}