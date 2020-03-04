package com.tokopedia.similarsearch.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

internal fun RecyclerView.ViewHolder?.setFullSpanStaggeredGrid() {
    if (this == null) return

    val layoutParams = this.itemView.layoutParams

    if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
        layoutParams.isFullSpan = true
    }
}
