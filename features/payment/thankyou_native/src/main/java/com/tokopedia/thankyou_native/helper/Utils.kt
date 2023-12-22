package com.tokopedia.thankyou_native.helper

import android.graphics.Path
import android.graphics.RectF
import android.view.View
import android.view.ViewOutlineProvider

fun View.radiusClip(topLeftRadius: Float, topRightRadius: Float, bottomRightRadius: Float, bottomLeftRadius: Float) {
    clipToOutline = true // Enable clipping to the outline

    // Create a custom outline provider to set corner radii
    outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: android.graphics.Outline) {
            val rect = RectF(0f, 0f, view.width.toFloat(), view.height.toFloat())
            val path = Path()
            path.addRoundRect(rect, floatArrayOf(
                topLeftRadius, topLeftRadius,
                topRightRadius, topRightRadius,
                bottomRightRadius, bottomRightRadius,
                bottomLeftRadius, bottomLeftRadius
            ), Path.Direction.CW)
            outline.setConvexPath(path)
        }
    }
}
