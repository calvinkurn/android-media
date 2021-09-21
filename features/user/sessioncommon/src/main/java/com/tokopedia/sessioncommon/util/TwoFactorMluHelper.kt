package com.tokopedia.sessioncommon.util

import android.content.Context

object TwoFactorMluHelper {
    fun clear2FaInterval(context: Context?) {
        val prefName = "user_additional_check"
        context?.let {
            val sharedPrefs = it.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            sharedPrefs.edit().clear().apply()
        }
    }
}