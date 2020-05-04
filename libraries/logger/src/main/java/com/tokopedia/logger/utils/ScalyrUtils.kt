package com.tokopedia.logger.utils

import android.content.Context
import java.util.*

object ScalyrUtils {

    const val SCALYR_PREF_NAME = "scalyr_pref"
    private const val SCALYR_SESSION_KEY = "session"

    fun getLogSession(context: Context, priority: Int): String {
        val sessionKey = SCALYR_SESSION_KEY + priority
        val sharedPreferences = context.getSharedPreferences(SCALYR_PREF_NAME, Context.MODE_PRIVATE)
        if (sharedPreferences.getString(sessionKey, "")?.isEmpty() == null) {
            val randomUUID = UUID.randomUUID().toString()
            sharedPreferences.edit().putString(sessionKey, randomUUID).apply()
            return randomUUID
        }
        return ""
    }
}
