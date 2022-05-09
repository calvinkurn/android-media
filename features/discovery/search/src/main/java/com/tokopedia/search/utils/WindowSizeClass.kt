package com.tokopedia.search.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics

enum class WindowSizeClass { COMPACT, MEDIUM, EXPANDED }

fun getWindowSizeClass(context: Context?): WindowSizeClass {
    if (context !is Activity) return WindowSizeClass.COMPACT

    val displayMetric = DisplayMetrics()
    context.windowManager.defaultDisplay.getMetrics(displayMetric)
    val screenWidthInDp = displayMetric.widthPixels / displayMetric.density

    return when {
        screenWidthInDp < 600f -> WindowSizeClass.COMPACT
        screenWidthInDp < 840f -> WindowSizeClass.MEDIUM
        else -> WindowSizeClass.EXPANDED
    }
}