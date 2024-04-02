package com.tokopedia.analytics.byteio.topads.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

fun getVisibleHeightPercentage(recyclerView: RecyclerView?, itemView: View?): String {

    if (itemView == null || recyclerView == null) return "0"

    val globalRect = Rect()
    recyclerView.getGlobalVisibleRect(globalRect)

    val itemRect = Rect()
    itemView.getLocalVisibleRect(itemRect)

    var percentFirst: Int
    percentFirst = if (itemRect.bottom >= globalRect.bottom) {
        val visibleHeightFirst: Int = globalRect.bottom - itemRect.top
        visibleHeightFirst * 100 / itemView.height
    } else {
        val visibleHeightFirst: Int = itemRect.bottom - globalRect.top
        visibleHeightFirst * 100 / itemView.height
    }

    if (percentFirst > 100) {
        percentFirst = 100
    }

    return percentFirst.toString()
}
