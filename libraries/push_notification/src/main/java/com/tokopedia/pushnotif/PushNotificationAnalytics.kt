package com.tokopedia.pushnotif

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.tokopedia.config.GlobalConfig
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.pushnotif.data.model.ApplinkNotificationModel
import com.tokopedia.remoteconfig.RemoteConfigKey
import timber.log.Timber

object PushNotificationAnalytics {
    @JvmStatic
    fun logEvent(context: Context, model: ApplinkNotificationModel, data: Bundle, message: String) {
        try {
            val whiteListedUsers = FirebaseRemoteConfig.getInstance().getString(RemoteConfigKey.WHITELIST_USER_LOG_NOTIFICATION)
            val userId = model.toUserId
            if (!userId.isEmpty() && whiteListedUsers.contains(userId)) {
                executeCrashlyticLog(context, data, message)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    @JvmStatic
    fun logEventFromAll(context: Context, data: Bundle, message: String) {
        try {
            executeCrashlyticLog(context, data, message)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    private fun executeCrashlyticLog(context: Context, data: Bundle, message: String) {
        if (!BuildConfig.DEBUG) {
            val logMessage = generateLogMessage(context, data, message)
            FirebaseCrashlytics.getInstance().recordException(Exception(logMessage))
            ServerLogger.log(Priority.P2, "LOG_PUSH_NOTIF", mapOf("type" to "PushNotification::notify(Context context, Bundle data)",
                    "data" to logMessage
            ))
        }
    }

    private fun generateLogMessage(context: Context, data: Bundle, message: String): String {
        val logMessage = StringBuilder("$message \n")
        val fcmToken = getFcmTokenFromPref(context)
        addLogLine(logMessage, "fcmToken", fcmToken)
        addLogLine(logMessage, "isSellerApp", GlobalConfig.isSellerApp())
        for (key in data.keySet()) {
            addLogLine(logMessage, key, data[key])
        }
        return logMessage.toString()
    }

    private fun getFcmTokenFromPref(context: Context): String? {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("pref_fcm_token", "")
    }

    private fun addLogLine(stringBuilder: StringBuilder, key: String, value: Any?) {
        stringBuilder.append(key)
        stringBuilder.append(": ")
        stringBuilder.append(value)
        stringBuilder.append(", \n")
    }
}