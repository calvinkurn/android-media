package com.tokopedia.notifications

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import com.google.firebase.messaging.RemoteMessage
import com.tokopedia.abstraction.common.utils.view.CommonUtils
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.CMConstant.NotificationType.DELETE_NOTIFICATION
import com.tokopedia.notifications.common.PayloadConverter
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.notifications.factory.CMNotificationFactory
import com.tokopedia.notifications.inApp.CMInAppManager
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.NotificationMode
import com.tokopedia.notifications.model.NotificationStatus
import com.tokopedia.notifications.database.pushRuleEngine.PushRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext
import androidx.work.WorkManager
import androidx.work.PeriodicWorkRequest
import com.tokopedia.notifications.worker.PushWorker
import java.util.concurrent.TimeUnit


/**
 * Created by Ashwani Tyagi on 18/10/18.
 */
class CMPushNotificationManager : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Job()

    private val TAG = CMPushNotificationManager::class.java.canonicalName

    private lateinit var applicationContext: Context

    private var cmUserHandler: CMUserHandler? = null

    private val isBackgroundTokenUpdateEnabled: Boolean
        get() = (applicationContext as CMRouter).getBooleanRemoteConfig("app_cm_token_capture_background_enable", true)

    private val remoteDelaySeconds: Long
        get() = (applicationContext as CMRouter).getLongRemoteConfig("app_token_send_delay", 60)


    private val isForegroundTokenUpdateEnabled: Boolean
        get() = (applicationContext as CMRouter).getBooleanRemoteConfig("app_cm_token_capture_foreground_enable", true)

    private val isPushEnable: Boolean
        get() = (applicationContext as CMRouter).getBooleanRemoteConfig("app_cm_push_enable", false) || BuildConfig.DEBUG

    private val isInAppEnable: Boolean
        get() = (applicationContext as CMRouter).getBooleanRemoteConfig(CMConstant.RemoteKeys.KEY_IS_INAPP_ENABLE,
                false) || BuildConfig.DEBUG

    /**
     * initialization of push notification library
     * Push Worker is initialisation & scheduled periodic
     * @param application
     */
    fun init(application: Application) {
        this.applicationContext = application.applicationContext
        CMInAppManager.getInstance().init(application)
        GraphqlClient.init(applicationContext)
        PushWorker.schedulePeriodicWorker()
    }

    /**
     * Send FCM token to server
     *
     * @param token
     */
    fun refreshFCMTokenFromForeground(token: String?, isForce: Boolean?) {
        try {

            if (::applicationContext.isInitialized && token != null && isForegroundTokenUpdateEnabled) {
                CommonUtils.dumper("token: $token")
                if (TextUtils.isEmpty(token)) {
                    return
                }
                cmUserHandler?.cancelRunnable()
                cmUserHandler = CMUserHandler(applicationContext)
                if (isForce == null) {
                    cmUserHandler?.updateToken(token, remoteDelaySeconds, false)
                } else {
                    cmUserHandler?.updateToken(token, remoteDelaySeconds, isForce)
                }
            }
        } catch (e: Exception) {
        }
    }

    /**
     * Send FCM token to server
     *
     * @param token
     */
    fun refreshTokenFromBackground(token: String?, isForce: Boolean?) {
        try {
            if (::applicationContext.isInitialized && token != null && isBackgroundTokenUpdateEnabled) {
                CommonUtils.dumper("token: $token")
                if (TextUtils.isEmpty(token)) {
                    return
                }
                cmUserHandler?.cancelRunnable()
                cmUserHandler = CMUserHandler(applicationContext)
                if (isForce == null) {
                    cmUserHandler?.updateToken(token, remoteDelaySeconds, false)
                } else {
                    cmUserHandler?.updateToken(token, remoteDelaySeconds, isForce)
                }
            }
        } catch (e: Exception) {
        }
    }

    /**
     * To check weather the incoming notification belong to campaign management
     *
     * @param extras
     * @return
     */
    fun isFromCMNotificationPlatform(extras: Map<String, String>?): Boolean {
        try {
            if (null != extras && extras.containsKey(CMConstant.PayloadKeys.SOURCE)) {
                val confirmationValue = extras[CMConstant.PayloadKeys.SOURCE]
                return confirmationValue == CMConstant.PayloadKeys.FCM_EXTRA_CONFIRMATION_VALUE ||
                        confirmationValue == CMConstant.PayloadKeys.SOURCE_VALUE
            }
        } catch (e: Exception) {
            Log.e(TAG, "CMPushNotificationManager: isFromCMNotificationPlatform", e)
        }

        return false
    }

    /**
     * Handle the remote message and show push notification
     *
     * @param remoteMessage
     */
    fun handlePushPayload(remoteMessage: RemoteMessage?) {
        if (null == remoteMessage)
            return

        if (null == remoteMessage.data)
            return

        try {
            if (isFromCMNotificationPlatform(remoteMessage.data)) {
                val confirmationValue = remoteMessage.data[CMConstant.PayloadKeys.SOURCE]
                val bundle = PayloadConverter.convertMapToBundle(remoteMessage.data)
                if (confirmationValue.equals(CMConstant.PayloadKeys.SOURCE_VALUE) && isInAppEnable) {
                    CMInAppManager.getInstance().handlePushPayload(remoteMessage)
                } else {
                    launchCatchError(
                            block = {
                                if (isPushEnable)
                                    handleNotificationBundle(bundle)
                            }, onError = {
                        Log.e(TAG, "CMPushNotificationManager: handleNotificationBundle ", it)
                    })
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "CMPushNotificationManager: handlePushPayload ", e)
        }

    }

    private fun handleNotificationBundle(bundle: Bundle) {
        try {
            //todo event notification received offline
            val baseNotificationModel = PayloadConverter.convertToBaseModel(bundle)
            if (baseNotificationModel.notificationMode == NotificationMode.OFFLINE) {
                onOfflinePushPayloadReceived(baseNotificationModel)
            } else {
                onLivePushPayloadReceived(baseNotificationModel)
            }
        } catch (e: Exception) {
        }
    }

    private fun onLivePushPayloadReceived(baseNotificationModel: BaseNotificationModel) {
        if (baseNotificationModel.type == DELETE_NOTIFICATION)
            baseNotificationModel.status = NotificationStatus.COMPLETED
        else if (baseNotificationModel.startTime == 0L
                || baseNotificationModel.endTime > System.currentTimeMillis()) {
            createAndPostNotification(baseNotificationModel)
            baseNotificationModel.status = NotificationStatus.ACTIVE
        } else {
            baseNotificationModel.status = NotificationStatus.COMPLETED
        }
        PushRepository.getInstance(applicationContext)
                .insertNotificationModel(baseNotificationModel)
    }

    private fun onOfflinePushPayloadReceived(baseNotificationModel: BaseNotificationModel) {
        if (baseNotificationModel.type == DELETE_NOTIFICATION)
            baseNotificationModel.status = NotificationStatus.DELETE
        else
            baseNotificationModel.status = NotificationStatus.PENDING
        PushRepository.getInstance(applicationContext).insertNotificationModel(baseNotificationModel)
    }

    fun postOfflineNotification(baseNotificationModel: BaseNotificationModel) {
        createAndPostNotification(baseNotificationModel)
        baseNotificationModel.status = NotificationStatus.ACTIVE
        PushRepository.getInstance(applicationContext).updateNotificationModel(baseNotificationModel)
    }

    fun cancelOfflineNotification(baseNotificationModel: BaseNotificationModel) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(baseNotificationModel.notificationId)
        baseNotificationModel.status = NotificationStatus.COMPLETED
        PushRepository.getInstance(applicationContext).updateNotificationModel(baseNotificationModel)
    }

    private fun createAndPostNotification(baseNotificationModel: BaseNotificationModel) {
        try {
            val baseNotification = CMNotificationFactory
                    .getNotification(instance.applicationContext, baseNotificationModel)
            if (null != baseNotification) {
                val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val notification = baseNotification.createNotification()
                notificationManager.notify(baseNotification.baseNotificationModel.notificationId, notification)
            }
        } catch (e: Exception) {
        }
    }

    companion object {
        @JvmStatic
        val instance: CMPushNotificationManager = CMPushNotificationManager()
    }

}
