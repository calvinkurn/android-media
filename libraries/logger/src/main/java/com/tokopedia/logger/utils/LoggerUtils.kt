package com.tokopedia.logger.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import java.util.*

object LoggerUtils {

    private const val REGEX_ALPHA_NUMERIC = "[^a-zA-Z0-9]"
    private const val PART_DEVICE_ID_LENGTH = 16
    private const val SUFFIX_SESSION = "ss"

    const val SCALYR_PREF_NAME = "logger_pref"
    private const val SCALYR_SESSION_KEY = "log_session"

    fun getLogSession(context: Context): String {
        val sessionKey = SCALYR_SESSION_KEY
        val sharedPreferences = context.getSharedPreferences(SCALYR_PREF_NAME, Context.MODE_PRIVATE)
        var session = sharedPreferences.getString(sessionKey, "")
        if (session == null || session.isEmpty()) {
            session = UUID.randomUUID().toString()
            sharedPreferences.edit().putString(sessionKey, session).apply()
        }
        return session
    }

    fun getPartDeviceId(context: Context): String {
        var deviceId = getAndroidId(context)
        if (deviceId.isEmpty()) {
            deviceId = getLogSession(context) + SUFFIX_SESSION
        }
        deviceId = deviceId.replace(REGEX_ALPHA_NUMERIC.toRegex(), "")
        if (deviceId.length > PART_DEVICE_ID_LENGTH) {
            deviceId = deviceId.substring(deviceId.length - PART_DEVICE_ID_LENGTH)
        }
        return deviceId
    }

    @SuppressLint("HardwareIds")
    private fun getAndroidId(context: Context): String {
        var androidId = ""
        try {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)?.let {
                androidId =it
            }
        } catch (e: Exception) {
            // No-Op
        }
        return androidId
    }
}
