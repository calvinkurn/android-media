package com.tokopedia.sellerhome.common

import android.content.Context
import com.tokopedia.kotlin.extensions.view.dpToPx

/**
 * Created By @ilhamsuaib on 2020-02-28
 */

object StatusbarHelper {

    fun getStatusBarHeight(context: Context): Int {
        var height = context.dpToPx(56).toInt()
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resId > 0) {
            height = context.resources.getDimensionPixelSize(resId)
        }
        return height
    }
}