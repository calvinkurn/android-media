package com.tokopedia.test.application.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import com.tokopedia.instrumentation.test.R
import java.lang.Exception

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
    fun getScreenWidth(context: Context): Int {
        val screenRes = getScreenResolution(context)
        val result = try {
            screenRes.split(",")[0].toInt()
        } catch (e: Exception) {
            0
        }
        return result
    }

    @JvmStatic
    fun getScreenHeight(context: Context): Int {
        val screenRes = getScreenResolution(context)
        val result = try {
            screenRes.split(",")[1].toInt()
        } catch (e: Exception) {
            0
        }
        return result
    }

    @JvmStatic
    fun isTablet(context: Context): Boolean {
        return context.resources.getBoolean(com.tokopedia.device.info.R.bool.isTablet)
    }
}
