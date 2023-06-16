@file:Suppress("UNCHECKED_CAST")

package com.tokopedia.scp_rewards.common.utils

import android.content.Context
import android.util.DisplayMetrics
import org.json.JSONObject

fun dpToPx(context: Context, dp: Int): Float {
    return dp * (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT).toFloat()
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
