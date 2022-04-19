package com.tokopedia.floatingwindow.util

import android.content.res.Resources
import android.util.DisplayMetrics

/**
 * Created by jegul on 27/11/20
 */
class ScreenLayoutHelper {

    private val displayMetrics = getCurrentDisplayMetrics()

    val widthPixels: Int
        get() = displayMetrics.widthPixels

    val heightPixels: Int
        get() = displayMetrics.heightPixels

    private fun getCurrentDisplayMetrics(): DisplayMetrics {
        return Resources.getSystem().displayMetrics
    }
}