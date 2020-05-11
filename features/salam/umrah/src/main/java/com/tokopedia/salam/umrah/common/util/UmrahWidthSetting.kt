package com.tokopedia.salam.umrah.common.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager


object UmrahWidthSetting{
    private fun getScreenWidth(context: Context): Float {
        val metrics = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        wm.defaultDisplay.getMetrics(metrics)
        val widthDpi = metrics.xdpi
        val heightDpi = metrics.ydpi

        val widthPixels = metrics.widthPixels
        val heightPixels = metrics.heightPixels

        val widthInches = widthPixels / widthDpi
        val heightInches = heightPixels / heightDpi

        return Math.sqrt(widthInches.toDouble() * widthInches + heightInches * heightInches).toFloat()
    }

    fun tabSize(context: Context): Boolean{
        return (getScreenWidth(context) >= 7)
    }
}