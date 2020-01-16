package com.tokopedia.device.info

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import com.tokopedia.device_info.R


/**
 * Created by mzennis on 2020-01-16.
 */
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