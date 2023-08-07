package com.tokopedia.scp_rewards_common

import android.graphics.Color
import org.json.JSONObject
import com.google.firebase.crashlytics.FirebaseCrashlytics

fun parseColor(color: String?): Int? {
    return try {
        Color.parseColor(color)
    } catch (err: Exception) {
        FirebaseCrashlytics.getInstance().recordException(err)
        null
    }
}

fun <T> String.parseJsonKey(key: String): T? {
    JSONObject(this).apply {
        return try {
            get(key) as T
        } catch (err: Throwable) {
            FirebaseCrashlytics.getInstance().recordException(err)
            null
        }
    }
}
