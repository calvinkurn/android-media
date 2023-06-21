package com.tokopedia.scp_rewards_touchpoints.bottomsheet.utils

import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics

fun dpToPx(context: Context, dp: Int): Float {
    return dp * (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT).toFloat()
}

fun parseColor(color: String?): Int? {
    return try {
        Color.parseColor(color)
    } catch (err: Exception) {
        null
    }
}
