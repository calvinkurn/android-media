package com.tokopedia.analytics.byteio.topads.util

import android.graphics.Rect
import android.view.View
import kotlin.math.max
import kotlin.math.roundToInt

fun getVisibleSizePercentage(itemView: View?): String {
    if (itemView == null) return "0"

    val itemRect = Rect()
    itemView.getGlobalVisibleRect(itemRect)

    val totalArea = itemView.width.toFloat() * itemView.height.toFloat()

    val visibleHeight = max(0, itemRect.bottom - itemRect.top)
    val visibleWidth = max(0, itemRect.right - itemRect.left)

    val visibleArea = visibleHeight * visibleWidth

    val visibleSizeAreaPercentage = if (totalArea <= 0f) {
        0f
    } else {
        (visibleArea / totalArea) * 100
    }

    return visibleSizeAreaPercentage.roundToInt().toString()
}
