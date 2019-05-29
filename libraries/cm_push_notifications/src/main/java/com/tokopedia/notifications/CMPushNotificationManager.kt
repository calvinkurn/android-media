package com.tokopedia.notifications

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log

import com.google.firebase.messaging.RemoteMessage
import com.tokopedia.abstraction.common.utils.view.CommonUtils
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.notifications.common.CMConstant

/**
 * Created by Ashwani Tyagi on 18/10/18.
 */
class CMPushNotificationManager {

    private val TAG = CMPushNotificationManager::class.java.canonicalName

    internal var applicationContext: Context? = null

    private var cmUserHandler: CMUserHandler? = null

    private val isBackgroundTokenUpdateEnabled: Boolean
        get() = (applicationContext as CMRouter).getBooleanRemoteConfig("app_cm_token_capture_background_enable", true)

    private val remoteDelaySeconds: Long
        get() = (applicationContext as CMRouter).getLongRemoteConfig("app_token_send_delay", 60)


    private val isForegroundTokenUpdateEnabled: Boolean
        get() = (applicationContext as CMRouter).getBooleanRemoteConfig("app_cm_token_capture_foreground_enable", true)

    /**
     * initialization of push notification library
     *
     * @param context
     */
    fun init(context: Context) {
        if (context == null) {
            throw IllegalArgumentException("Context can not be null")
        }
        this.applicationContext = context.applicationContext
        GraphqlClient.init(applicationContext!!)
    }

    /**
     * Send FCM token to server
     *
     * @param token
     */
    fun refreshFCMTokenFromForeground(token: String, isForce: Boolean) {
        if (isForegroundTokenUpdateEnabled) {
            CommonUtils.dumper("token: $token")
            if (applicationContext == null) {
                return
            }
            if (TextUtils.isEmpty(token)) {
                return
            }
            if (cmUserHandler != null)
                cmUserHandler!!.cancelRunnable()
            cmUserHandler = CMUserHandler(applicationContext)
            cmUserHandler!!.updateToken(token, remoteDelaySeconds, isForce)
        }
    }

    /**
     * Send FCM token to server
     *
     * @param token
     */
    fun refreshTokenFromBackground(token: String, force: Boolean) {
        if (isBackgroundTokenUpdateEnabled) {
            CommonUtils.dumper("token: $token")
            if (applicationContext == null) {
                return
            }
            if (TextUtils.isEmpty(token)) {
                return
            }
            if (cmUserHandler != null)
                cmUserHandler!!.cancelRunnable()
            cmUserHandler = CMUserHandler(applicationContext)
            cmUserHandler!!.updateToken(token, remoteDelaySeconds, force)
        }
    }


    /**
     * To check weather the incoming notification belong to campaign management
     *
     * @param extras
     * @return
     */
    fun isFromCMNotificationPlatform(extras: Map<String, String>?): Boolean {
        if (applicationContext == null) {
            throw IllegalArgumentException("Kindly invoke init before calling notification library")
        }
        try {
            if (null != extras && extras.containsKey(CMConstant.PayloadKeys.SOURCE)) {
                val confirmationValue = extras[CMConstant.PayloadKeys.SOURCE]
                return confirmationValue == CMConstant.PayloadKeys.FCM_EXTRA_CONFIRMATION_VALUE
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
        if (applicationContext == null) {
            throw IllegalArgumentException("Kindly invoke init before calling notification library")
        }
        if (null == remoteMessage.data)
            return
        try {
            if (isFromCMNotificationPlatform(remoteMessage.data)) {
                val bundle = convertMapToBundle(remoteMessage.data)
                CMJobIntentService.enqueueWork(applicationContext!!, bundle)
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

    companion object {
        @JvmStatic
        val instance: CMPushNotificationManager = CMPushNotificationManager()
    }

}
