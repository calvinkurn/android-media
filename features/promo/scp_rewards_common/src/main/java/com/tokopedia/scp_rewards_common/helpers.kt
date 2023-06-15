package com.tokopedia.scp_rewards_common

import android.graphics.Color

fun parseColor(color: String?): Int? {
    return try {
        Color.parseColor(color)
    } catch (err: Exception) {
        null
    }
}
