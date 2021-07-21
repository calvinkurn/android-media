package com.tokopedia.selleronboarding.old.utils

import android.content.Context
import com.tokopedia.kotlin.extensions.view.dpToPx

/**
 * Created By @ilhamsuaib on 09/04/20
 */

object StatusBarHelper {

    private const val STATUS_BAR_DEFAULT_HEIGHT = 24

    fun getStatusBarHeight(context: Context): Int {
        var height = context.dpToPx(STATUS_BAR_DEFAULT_HEIGHT).toInt()
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resId > 0) {
            height = context.resources.getDimensionPixelSize(resId)
        }
        return height
    }
}