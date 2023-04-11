package com.tokopedia.notifications

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.google.firebase.messaging.RemoteMessage
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.interceptors.authenticator.TkpdAuthenticatorGql
import com.tokopedia.interceptors.refreshtoken.RefreshTokenGql
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.ServerLogger.log
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.NetworkRouter
import com.tokopedia.notification.common.PushNotificationApi
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.CMConstant.PayloadKeys.*
import com.tokopedia.notifications.common.CMRemoteConfigUtils
import com.tokopedia.notifications.common.HOURS_24_IN_MILLIS
import com.tokopedia.notifications.data.AmplificationDataSource
import com.tokopedia.notifications.inApp.CMInAppManager
import com.tokopedia.notifications.payloadProcessor.InAppPayloadPreprocessorUseCase
import com.tokopedia.notifications.payloadProcessor.NotificationPayloadPreProcessorUseCase
import com.tokopedia.notifications.push.PushController
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import timber.log.Timber
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
        get() = cmRemoteConfigUtils.getBooleanRemoteConfig(
            CMConstant.RemoteKeys.KEY_IS_INAPP_ENABLE,
            true
        ) || BuildConfig.DEBUG

    private var aidlApiBundle: Bundle? = null

    val cmPushEndTimeInterval: Long
        get() = cmRemoteConfigUtils.getLongRemoteConfig(
            CMConstant.RemoteKeys.KEY_CM_PUSH_END_TIME_INTERVAL,
            HOURS_24_IN_MILLIS * 7
        )

    val sellerAppCmAddTokenEnabled: Boolean
        get() = cmRemoteConfigUtils.getBooleanRemoteConfig(
            CMConstant.RemoteKeys.KEY_SELLERAPP_CM_ADD_TOKEN_ENABLED,
            false
        )

    /**
     * initialization of push notification library
     * Push Worker is initialisation & scheduled periodic
     * @param application
     */
    fun init(application: Application) {
        this.applicationContext = application.applicationContext
        CMInAppManager.getInstance().init(application)

        initGraphql(application)

        PushNotificationApi.bindService(
            applicationContext,
            ::onAidlReceive,
            ::onAidlError
        )

        getAmplificationPushData(application)
    }

    private fun initGraphql(application: Application) {
        val authenticator = TkpdAuthenticatorGql(
            application,
            application as NetworkRouter,
            UserSession(application),
            RefreshTokenGql()
        )
        GraphqlClient.init(application, authenticator)
    }

    private fun getAmplificationPushData(application: Application) {
        /*
         * Amplification of push notification.
         * fetch all of cm_push_notification's
         * push notification data that aren't rendered yet.
         * then, put all of push_data into local storage.
         * */
        if (getAmplificationRemoteConfig()) {
            try {
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
        return cmRemoteConfigUtils.getBooleanRemoteConfig(
            RemoteConfigKey.ENABLE_AMPLIFICATION,
            false
        )
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
                if (confirmationValue.equals(SOURCE_VALUE) && isInAppEnable) {
                    handleInAppFCMPayload(data)
                } else if (isPushEnable) {
                    handleNotificationFCMPayload(data)
                } else if (!(
                    confirmationValue.equals(SOURCE_VALUE) || confirmationValue.equals(
                            FCM_EXTRA_CONFIRMATION_VALUE
                        )
                    )
                ) {
                    ServerLogger.log(
                        Priority.P2,
                        "CM_VALIDATION",
                        mapOf(
                            "type" to "validation",
                            "reason" to "not_cm_source",
                            "data" to dataString.take(CMConstant.TimberTags.MAX_LIMIT)
                        )
                    )
                }
            }
        } catch (e: Exception) {
            ServerLogger.log(
                Priority.P2,
                "CM_VALIDATION",
                mapOf(
                    "type" to "exception",
                    "err" to Log.getStackTraceString(e)
                        .take(CMConstant.TimberTags.MAX_LIMIT),
                    "data" to dataString.take(CMConstant.TimberTags.MAX_LIMIT)
                )
            )
        }
    }

    private fun handleNotificationFCMPayload(map: Map<String, String>) {
        NotificationPayloadPreProcessorUseCase().getBaseNotificationModel(
            map,
            onSuccess = { baseNotificationModel, advanceTargetingData ->
                PushController(applicationContext).handleProcessedPushPayload(
                    aidlApiBundle,
                    baseNotificationModel,
                    advanceTargetingData
                )
            },
            onError = {
                // todo for server logging
            }
        )
    }

    fun handleNotificationJsonPayload(payloadString: String, isAmplification: Boolean) {
        NotificationPayloadPreProcessorUseCase().getBaseNotificationModel(
            payloadString,
            isAmplification,
            onSuccess = { baseNotificationModel, advanceTargetingData ->
                PushController(applicationContext).handleProcessedPushPayload(
                    aidlApiBundle,
                    baseNotificationModel,
                    advanceTargetingData
                )
            },
            onError = {
                // todo for server logging
            }
        )
    }

    /*Handle InAPP payload from FCM and GQL start*/
    private fun handleInAppFCMPayload(map: Map<String, String>) {
        InAppPayloadPreprocessorUseCase().getCMInAppModel(
            map,
            onSuccess = { cmInApp ->
                CMInAppManager.getInstance().handleCMInAppPushPayload(cmInApp)
            },
            onError = {
                // todo for server logging
            }
        )
    }

    fun handleInAppJsonPayload(payloadString: String, isAmplification: Boolean) {
        InAppPayloadPreprocessorUseCase().getCMInAppModel(
            payloadString,
            isAmplification,
            onSuccess = { cmInApp ->
                CMInAppManager.getInstance().handleCMInAppPushPayload(cmInApp)
            },
            onError = {
                // todo for server logging
            }
        )
    }
    /*Handle InAPP payload from FCM and GQL End*/

    companion object {

        @JvmStatic
        val instance: CMPushNotificationManager = CMPushNotificationManager()
    }
}
