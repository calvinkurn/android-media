package com.tokopedia.analytics.byteio.topads.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowOverModel
import com.tokopedia.analytics.byteio.topads.provider.IAdsLogShowOverProvider
import com.tokopedia.kotlin.extensions.view.orZero

fun getVisibleHeightPercentage(recyclerView: RecyclerView?, itemView: View?): String {

    if (itemView == null || recyclerView == null) return "0.0"

    val globalRect = Rect()
    recyclerView.getGlobalVisibleRect(globalRect)

    val itemRect = Rect()
    val isChildViewNotEmpty = itemView.getLocalVisibleRect(itemRect)

    val visibleHeight = itemRect.height().toFloat()
    val originHeight = itemView.measuredHeight

    val viewVisibleHeightPercentage = visibleHeight / (originHeight * 100)

    return if (isChildViewNotEmpty) {
        viewVisibleHeightPercentage
    } else {
        0.0f
    }.toString()
}
