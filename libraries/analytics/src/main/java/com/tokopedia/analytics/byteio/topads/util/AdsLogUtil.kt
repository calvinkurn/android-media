package com.tokopedia.analytics.byteio.topads.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max
import kotlin.math.min

fun getVisibleHeightPercentage(recyclerView: RecyclerView?, itemView: View?): String {

    if (itemView == null || recyclerView == null) return "0"

    val globalRect = Rect()
    recyclerView.getGlobalVisibleRect(globalRect)

    val itemRect = Rect()
    itemView.getLocalVisibleRect(itemRect)

    val visibleHeightItem = if (itemRect.bottom >= globalRect.bottom) {
        max(0, globalRect.bottom - itemRect.top)
    } else {
        max(0, itemRect.bottom - globalRect.top)
    }

    val percentHeight = (visibleHeightItem * 100.0 / itemView.height).toInt()

    val cappedPercent = min(percentHeight, 100)

    return cappedPercent.toString()
}
