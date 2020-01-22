package com.tokopedia.device.info

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

object DeviceScreenInfo {

    @JvmStatic
    fun getScreenResolution(context: Context): String {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        val width = metrics.widthPixels
        val height = metrics.heightPixels

        return "$width,$height"
    }

    @JvmStatic
    fun isTablet(context: Context): Boolean {
        return context.resources.getBoolean(R.bool.isTablet)
    }
}