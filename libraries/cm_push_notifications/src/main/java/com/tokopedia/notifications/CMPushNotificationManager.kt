package com.tokopedia.notifications

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.google.firebase.messaging.RemoteMessage
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.ServerLogger.log
import com.tokopedia.logger.utils.Priority
import com.tokopedia.notification.common.PushNotificationApi
import com.tokopedia.notification.common.utils.NotificationValidationManager
import com.tokopedia.notifications.common.*
import com.tokopedia.notifications.common.CMConstant.PayloadKeys.*
import com.tokopedia.notifications.common.PayloadConverter.advanceTargetNotification
import com.tokopedia.notifications.common.PayloadConverter.convertMapToBundle
import com.tokopedia.notifications.data.AmplificationDataSource
import com.tokopedia.notifications.inApp.CMInAppManager
import com.tokopedia.notifications.model.NotificationMode
import com.tokopedia.notifications.worker.PushWorker
import com.tokopedia.remoteconfig.RemoteConfigKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import timber.log.Timber
import java.util.*
import kotlin.coroutines.CoroutineContext

/**
 * Created by Ashwani Tyagi on 18/10/18.
 */
class CMPushNotificationManager : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Job()

    private val TAG = CMPushNotificationManager::class.java.canonicalName

    private lateinit var applicationContext: Context

    private var cmUserHandler: CMUserHandler? = null

    private val cmRemoteConfigUtils by lazy { CMRemoteConfigUtils(applicationContext) }

    private val isBackgroundTokenUpdateEnabled: Boolean
        get() = cmRemoteConfigUtils.getBooleanRemoteConfig("app_cm_token_capture_background_enable", true)

    private val remoteDelaySeconds: Long
        get() = cmRemoteConfigUtils.getLongRemoteConfig("app_token_send_delay", 60)


    private val isForegroundTokenUpdateEnabled: Boolean
        get() = cmRemoteConfigUtils.getBooleanRemoteConfig("app_cm_token_capture_foreground_enable", true)

    private val isPushEnable: Boolean
        get() = cmRemoteConfigUtils.getBooleanRemoteConfig(CMConstant.RemoteKeys.KEY_IS_CM_PUSH_ENABLE, true) || BuildConfig.DEBUG

    private val isInAppEnable: Boolean
        get() = cmRemoteConfigUtils.getBooleanRemoteConfig(CMConstant.RemoteKeys.KEY_IS_INAPP_ENABLE,
                true) || BuildConfig.DEBUG

    private var aidlApiBundle: Bundle? = null

    val cmPushEndTimeInterval: Long
        get() = cmRemoteConfigUtils.getLongRemoteConfig(CMConstant.RemoteKeys.KEY_CM_PUSH_END_TIME_INTERVAL,
                HOURS_24_IN_MILLIS * 7)

    val sellerAppCmAddTokenEnabled: Boolean
        get() = cmRemoteConfigUtils.getBooleanRemoteConfig(CMConstant.RemoteKeys.KEY_SELLERAPP_CM_ADD_TOKEN_ENABLED,
                false)

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

        PushNotificationApi.bindService(
                applicationContext,
                ::onAidlReceive,
                ::onAidlError
        )

        getAmplificationPushData(application)
    }


    private fun getAmplificationPushData(application: Application) {
        /*
         * Amplification of push notification.
         * fetch all of cm_push_notification's
         * push notification data that aren't rendered yet.
         * then, put all of push_data into local storage.
         * */
        println("Amplification API before remote config")
        if (getAmplificationRemoteConfig()) {
            try {
                println("Amplification API after remote config")
                AmplificationDataSource.invoke(application)
            } catch (e: java.lang.Exception) {
                val messageMap: MutableMap<String, String> = HashMap()
                messageMap["type"] = "exception"
                messageMap["err"] = Log.getStackTraceString(e).take(CMConstant.TimberTags.MAX_LIMIT)
                messageMap["data"] = ""
                log(Priority.P2, "CM_VALIDATION", messageMap)
            }
        }
    }

    private fun getAmplificationRemoteConfig(): Boolean {
        return cmRemoteConfigUtils.getBooleanRemoteConfig(RemoteConfigKey.ENABLE_AMPLIFICATION, false)
    }

    private fun onAidlReceive(tag: String, bundle: Bundle?) {
        this.aidlApiBundle = bundle
    }

    private fun onAidlError() {}

    /**
     * Send FCM token to server
     *
     * @param token
     */
    fun refreshFCMTokenFromForeground(token: String?, isForce: Boolean?) {
        try {

            if (::applicationContext.isInitialized && token != null && isForegroundTokenUpdateEnabled) {
                Timber.d("token: $token")
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
                Timber.d("token: $token")
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
            if (null != extras && extras.containsKey(SOURCE)) {
                val confirmationValue = extras[SOURCE]
                return confirmationValue == FCM_EXTRA_CONFIRMATION_VALUE ||
                        confirmationValue == SOURCE_VALUE
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
        if (remoteMessage == null) return

        val data = remoteMessage.data
        val dataString = data.toString()

        try {
            if (isFromCMNotificationPlatform(data)) {
                val confirmationValue = data[SOURCE]
                val bundle = convertMapToBundle(data)

                if (confirmationValue.equals(SOURCE_VALUE) && isInAppEnable) {
                    CMInAppManager.getInstance().handlePushPayload(remoteMessage)
                } else if (isPushEnable) {
                    validateAndRenderNotification(bundle)
                } else if (!(confirmationValue.equals(SOURCE_VALUE) || confirmationValue.equals(FCM_EXTRA_CONFIRMATION_VALUE))){
                    ServerLogger.log(Priority.P2, "CM_VALIDATION",
                            mapOf("type" to "validation", "reason" to "not_cm_source", "data" to dataString.take(CMConstant.TimberTags.MAX_LIMIT)))
                }
            }
        } catch (e: Exception) {
            ServerLogger.log(Priority.P2, "CM_VALIDATION",
                    mapOf("type" to "exception",
                            "err" to Log.getStackTraceString(e)
                            .take(CMConstant.TimberTags.MAX_LIMIT),
                            "data" to dataString.take(CMConstant.TimberTags.MAX_LIMIT)))
        }
    }

    private fun validateAndRenderNotification(notification: Bundle) {

        val baseNotificationModel = PayloadConverter.convertToBaseModel(notification)
        if (baseNotificationModel.notificationMode != NotificationMode.OFFLINE) {
            IrisAnalyticsEvents.sendPushEvent(applicationContext, IrisAnalyticsEvents.PUSH_RECEIVED, baseNotificationModel)
        }
        // aidlApiBundle : the data comes from AIDL service (including userSession data from another app)
        aidlApiBundle?.let { aidlBundle ->

            /*
            * getting the smart push notification data from payload such as:
            * mainAppPriority
            * sellerAppPriority
            * advanceTarget
            * */
            val targeting = advanceTargetNotification(notification)

            // the smart push notification validators
            NotificationValidationManager(applicationContext, targeting).validate(aidlBundle, {
                renderPushNotification(notification)
            }, {
                // set cancelled notification if isn't notified
                PushController(applicationContext).cancelPushNotification(notification)
            })

        }?: renderPushNotification(notification) // render as usual if there's no data from AIDL service
    }

    private fun renderPushNotification(bundle: Bundle) {
        PushController(applicationContext).handleNotificationBundle(bundle)
    }

    companion object {
        @JvmStatic
        val instance: CMPushNotificationManager = CMPushNotificationManager()
    }

}