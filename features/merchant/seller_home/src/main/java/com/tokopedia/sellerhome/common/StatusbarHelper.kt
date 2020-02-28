package com.tokopedia.sellerhome.common

import android.content.Context

/**
 * Created By @ilhamsuaib on 2020-02-28
 */

object StatusbarHelper {
    fun getStatusBarHeight(context: Context): Int {
        var height = 0
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resId > 0) {
            height = context.resources.getDimensionPixelSize(resId)
        }
        return height
    }
}