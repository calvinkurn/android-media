package com.tokopedia.scp_rewards_common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import kotlin.math.pow
import kotlin.math.sqrt

object DeviceInfo {
    @SuppressLint("DeprecatedMethod")
    fun getScreenSizeInInches(context: Context?): Double {
        if (context != null) {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val metrics = DisplayMetrics()
            wm.defaultDisplay.getRealMetrics(metrics)
            val widthPx = metrics.widthPixels
            val heightPx = metrics.heightPixels
            val widthInInches = widthPx.toDouble() / metrics.xdpi
            val heightInInches = heightPx.toDouble() / metrics.ydpi
            return sqrt(widthInInches.pow(2.toDouble()) + heightInInches.pow(2.toDouble()))
        }
        return 0.0
    }
}
