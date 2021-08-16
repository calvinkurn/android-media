package com.tokopedia.selleronboarding.utils

import android.util.DisplayMetrics
import android.view.View

internal const val IMG_DEVICE_SCREEN_PERCENT = 0.45F

internal fun View.setupMarginTitleSob(setMarginTitleSmallScreen:() -> Unit) {
    when (resources.displayMetrics.densityDpi) {
        DisplayMetrics.DENSITY_LOW, DisplayMetrics.DENSITY_MEDIUM, DisplayMetrics.DENSITY_HIGH, DisplayMetrics.DENSITY_XHIGH, DisplayMetrics.DENSITY_260,
        DisplayMetrics.DENSITY_280, DisplayMetrics.DENSITY_300, DisplayMetrics.DENSITY_340,
        DisplayMetrics.DENSITY_360 -> {
            setMarginTitleSmallScreen()
        }
    }
}