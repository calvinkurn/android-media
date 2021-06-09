package com.tokopedia.searchbar.navigation_component.util

import android.content.Context
import android.util.TypedValue

object NavToolbarExt {
    fun getToolbarHeight(context: Context): Int {
        val tv = TypedValue()
        return if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            val actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
            actionBarHeight
        } else {
            0
        }
    }

    fun getStatusbarHeight(context: Context): Int {
        var height = 0
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resId > 0) {
            height = context.resources.getDimensionPixelSize(resId)
        }
        return height
    }

    fun getFullToolbarHeight(context: Context): Int {
        return getToolbarHeight(context) + getStatusbarHeight(context)
    }
}