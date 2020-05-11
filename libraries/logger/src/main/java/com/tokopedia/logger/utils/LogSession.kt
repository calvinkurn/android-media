package com.tokopedia.logger.utils

import android.content.Context
import java.util.*

object LogSession {
    var sessionString = ""
    fun getLogSession(context: Context): String {
        if (sessionString.isEmpty()) {
            val sharedPreferences = context.getSharedPreferences(Constants.SCALYR_PREF_NAME, Context.MODE_PRIVATE)
            sessionString = sharedPreferences.getString(Constants.SCALYR_SESSION_KEY, "") ?: ""
            if (sessionString.isEmpty()) {
                sessionString = UUID.randomUUID().toString()
                sharedPreferences.edit().putString(Constants.SCALYR_SESSION_KEY, sessionString).apply()
            }
        }
        return sessionString
    }
}
