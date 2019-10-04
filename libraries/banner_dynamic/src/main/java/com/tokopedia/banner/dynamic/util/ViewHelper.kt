package com.tokopedia.banner.dynamic.util

import android.content.Context

/**
 * @author by furqan on 13/09/2019
 */
object ViewHelper {
    fun getStatusBarHeight(context: Context): Int {
        var height = 0
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resId > 0) {
            height = context.resources.getDimensionPixelSize(resId)
        }
        return height
    }
}