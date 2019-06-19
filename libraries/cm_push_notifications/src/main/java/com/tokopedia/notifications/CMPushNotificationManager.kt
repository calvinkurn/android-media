package com.tokopedia.notifications

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.google.firebase.messaging.RemoteMessage
import com.tokopedia.abstraction.common.utils.view.CommonUtils
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.notifications.factory.CMNotificationFactory
import com.tokopedia.notifications.inApp.CMInAppManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Created by Ashwani Tyagi on 18/10/18.
 */
class CMPushNotificationManager : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

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
        get() = (applicationContext as CMRouter).getBooleanRemoteConfig("app_cm_push_enable", false)

    /**
     * initialization of push notification library
     *
     * @param context
     */
    fun init(application: Application) {
        this.applicationContext = application.applicationContext
        CMInAppManager.getInstance().init(application)
        GraphqlClient.init(applicationContext)
    }

    /**
     * Send FCM token to server
     *
     * @param token
     */
    fun refreshFCMTokenFromForeground(token: String, isForce: Boolean) {
        try {
            if (isForegroundTokenUpdateEnabled) {
                CommonUtils.dumper("token: $token")
                if (TextUtils.isEmpty(token)) {
                    return
                }
                cmUserHandler?.cancelRunnable()
                cmUserHandler = CMUserHandler(applicationContext)
                cmUserHandler!!.updateToken(token, remoteDelaySeconds, isForce)
            }
        } catch (e: Exception) {
        }
    }

    /**
     * Send FCM token to server
     *
     * @param token
     */
    fun refreshTokenFromBackground(token: String, force: Boolean) {
        try {
            if (isBackgroundTokenUpdateEnabled) {
                CommonUtils.dumper("token: $token")
                if (TextUtils.isEmpty(token)) {
                    return
                }
                if (cmUserHandler != null)
                    cmUserHandler!!.cancelRunnable()
                cmUserHandler = CMUserHandler(applicationContext)
                cmUserHandler?.updateToken(token, remoteDelaySeconds, force)
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
        if (!isPushEnable)
            return
        if (null == remoteMessage)
            return

        if (null == remoteMessage.data)
            return

        try {
            if (isFromCMNotificationPlatform(remoteMessage.data)) {
                val confirmationValue = remoteMessage.data[CMConstant.PayloadKeys.SOURCE]
                val bundle = convertMapToBundle(remoteMessage.data)
                if (confirmationValue.equals(CMConstant.PayloadKeys.SOURCE_VALUE)) {
                    CMInAppManager.getInstance().handlePushPayload(remoteMessage)
                } else {
                    launchCatchError(
                            block = {
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

    private fun convertMapToBundle(map: Map<String, String>?): Bundle {
        val bundle = Bundle(map?.size ?: 0)
        if (map != null) {
            for ((key, value) in map) {
                bundle.putString(key, value)
            }
        }
        return bundle
    }

    private fun handleNotificationBundle(bundle: Bundle) {
        try {
            val applicationContext = instance.applicationContext
            val baseNotification = CMNotificationFactory
                    .getNotification(instance.applicationContext, bundle)
            if (null != baseNotification) {
                val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val notification = baseNotification.createNotification()
                notificationManager.notify(baseNotification.baseNotificationModel.notificationId, notification)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }

    companion object {
        @JvmStatic
        val instance: CMPushNotificationManager = CMPushNotificationManager()
    }

}
