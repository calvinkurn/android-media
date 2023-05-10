package com.tokopedia.abstraction.base.view.debugbanner

import android.content.Context
import android.graphics.Point
import android.view.View
import android.view.WindowManager

internal fun View.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()

internal fun Context.getScreenWidth(): Float {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.x.toFloat()
}
