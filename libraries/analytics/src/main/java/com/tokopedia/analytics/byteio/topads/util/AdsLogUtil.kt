package com.tokopedia.analytics.byteio.topads.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

fun getVisibleSizePercentage(recyclerView: RecyclerView?, itemView: View?): String {
    if (itemView == null || recyclerView == null) return "0"

    val globalRect = Rect()
    recyclerView.getGlobalVisibleRect(globalRect)

    val itemRect = Rect()
    itemView.getGlobalVisibleRect(itemRect)

    val totalArea = itemView.width.toFloat() * itemView.height.toFloat()

    val visibleHeight = max(0, min(itemRect.bottom, globalRect.bottom) - max(itemRect.top, globalRect.top))
    val visibleWidth = max(0, min(itemRect.right, globalRect.right) - max(itemRect.left, globalRect.left))
    val visibleArea = (visibleHeight * visibleWidth).toFloat()

    val visibleSizeAreaPercentage = if (totalArea <= 0f) {
        0f
    } else {
        (visibleArea / totalArea) * 100
    }

    return visibleSizeAreaPercentage.roundToInt().toString()
}
