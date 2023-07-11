package com.tokopedia.scp_rewards_common

import android.graphics.Color
import org.json.JSONObject

fun parseColor(color: String?): Int? {
    return try {
        Color.parseColor(color)
    } catch (err: Exception) {
        null
    }
}

fun <T> String.parseJsonKey(key: String): T? {
    JSONObject(this).apply {
        return try {
            get(key) as T
        } catch (err: Throwable) {
            null
        }
    }
}
