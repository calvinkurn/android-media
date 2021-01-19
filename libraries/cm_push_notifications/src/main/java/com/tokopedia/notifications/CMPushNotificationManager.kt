package com.tokopedia.notifications

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.google.firebase.messaging.RemoteMessage
import com.tokopedia.appaidl.AidlApi
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.CMRemoteConfigUtils
import com.tokopedia.notifications.common.HOURS_24_IN_MILLIS
import com.tokopedia.notifications.common.PayloadConverter
import com.tokopedia.notifications.inApp.CMInAppManager
import com.tokopedia.notifications.utils.NotificationValidationManager
import com.tokopedia.notifications.worker.PushWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

/**
 * Created by Ashwani Tyagi on 18/10/18.
 */
class CMPushNotificationManager : CoroutineScope, AidlApi.ReceiverListener {

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
        get() = cmRemoteConfigUtils.getBooleanRemoteConfig(CMConstant.RemoteKeys.KEY_IS_CM_PUSH_ENABLE, false) || BuildConfig.DEBUG

    private val isInAppEnable: Boolean
        get() = cmRemoteConfigUtils.getBooleanRemoteConfig(CMConstant.RemoteKeys.KEY_IS_INAPP_ENABLE,
                false) || BuildConfig.DEBUG

    private var aidlApiBundle: Bundle? = null

    val cmPushEndTimeInterval: Long
        get() = cmRemoteConfigUtils.getLongRemoteConfig(CMConstant.RemoteKeys.KEY_CM_PUSH_END_TIME_INTERVAL,
                HOURS_24_IN_MILLIS * 7)

    val sellerAppCmAddTokenEnabled: Boolean
        get() = cmRemoteConfigUtils.getBooleanRemoteConfig(CMConstant.RemoteKeys.KEY_SELLERAPP_CM_ADD_TOKEN_ENABLED,
                false)

    var aidlApiApp: AidlApi? = null
        private set

    /**
     * initialization of push notification library
     * Push Worker is initialisation & scheduled periodic
     * @param application
     */
    fun init(application: Application) {
        this.applicationContext = application.applicationContext
        aidlApiApp = AidlApi(this.applicationContext, this)
        CMInAppManager.getInstance().init(application)
        GraphqlClient.init(applicationContext)
        PushWorker.schedulePeriodicWorker()

        aidlApiApp?.bindService()
    }

    override fun onAidlReceive(tag: String, bundle: Bundle?) {
        this.aidlApiBundle = bundle
    }

    override fun onAidlError() {}

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

        val data = remoteMessage.data
        val appName = if (GlobalConfig.isSellerApp()) "sellerapp" else "mainapp"

        val dataString = data.toString()
        try {
            if (isFromCMNotificationPlatform(data)) {
                val confirmationValue = data[CMConstant.PayloadKeys.SOURCE]
                val bundle = PayloadConverter.convertMapToBundle(data)
                if (confirmationValue.equals(CMConstant.PayloadKeys.SOURCE_VALUE) && isInAppEnable) {
                    CMInAppManager.getInstance().handlePushPayload(remoteMessage)
                } else if (isPushEnable) {
                    val mockValidationByMessage = bundle.getString(CMConstant.PayloadKeys.MESSAGE, "")
                    Log.d("AIDL_App ($appName)", "handlePushPayload: get mockValidationByMessage: $mockValidationByMessage")
                    val validator = NotificationValidationManager(applicationContext, mockValidationByMessage)
                    aidlApiBundle?.let {
                        validator.validate(aidlApiBundle) {
                            Log.d("AIDL_App ($appName)", "validator.validate(aidlApiBundle)")
                            PushController(applicationContext).handleNotificationBundle(bundle)
                        }
                    }
                } else if (!(confirmationValue.equals(CMConstant.PayloadKeys.SOURCE_VALUE) || confirmationValue.equals(CMConstant.PayloadKeys.FCM_EXTRA_CONFIRMATION_VALUE))){
                    Timber.w("${CMConstant.TimberTags.TAG}validation;reason='not_cm_source';data='${dataString.
                    take(CMConstant.TimberTags.MAX_LIMIT)}'")
                }
            }
        } catch (e: Exception) {
            Timber.w( "${CMConstant.TimberTags.TAG}exception;err='${Log.getStackTraceString(e)
                    .take(CMConstant.TimberTags.MAX_LIMIT)}';data='${dataString.take(CMConstant.TimberTags.MAX_LIMIT)}'")
        }

    }

    companion object {
        @JvmStatic
        val instance: CMPushNotificationManager = CMPushNotificationManager()
    }

}