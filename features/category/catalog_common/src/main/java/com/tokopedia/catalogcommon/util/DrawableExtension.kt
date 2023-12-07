package com.tokopedia.catalogcommon.util

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable

object DrawableExtension {
    const val COLOR_TRANSPARENT = 0x00
    const val NO_RADIUS = 0F

    fun createGradientDrawable(
        colorTop: Int = COLOR_TRANSPARENT,
        colorBottom: Int = COLOR_TRANSPARENT,
        cornerRadius: Float = NO_RADIUS
    ): Drawable {
        return GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(colorTop, colorBottom)
        ).apply {
            this.cornerRadius = cornerRadius
        }
    }
}
