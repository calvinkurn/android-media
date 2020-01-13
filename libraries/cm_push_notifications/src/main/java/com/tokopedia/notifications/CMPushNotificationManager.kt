package com.tokopedia.notifications

import android.app.Application
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.google.firebase.messaging.RemoteMessage
import com.tokopedia.abstraction.common.utils.view.CommonUtils
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.PayloadConverter
import com.tokopedia.notifications.inApp.CMInAppManager
import com.tokopedia.notifications.worker.PushWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
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
                } else if (isPushEnable){
                    PushController(applicationContext).handleNotificationBundle(bundle)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "CMPushNotificationManager: handlePushPayload ", e)
        }

    }


    companion object {
        @JvmStatic
        val instance: CMPushNotificationManager = CMPushNotificationManager()
    }

}