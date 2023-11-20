package com.tokopedia.device.info

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
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
        return context.resources.getBoolean(R.bool.isTablet)
    }

    fun isFoldable(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_HINGE_ANGLE)
        } else {
            false
        }
    }
}
