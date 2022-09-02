package com.tokopedia.kotlin.extensions.view

import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.utils.scroll.RecyclerViewScrollListener

fun RecyclerView.smoothSnapToPosition(position: Int, snapMode: Int = LinearSmoothScroller.SNAP_TO_START) {
    val smoothScroller = object : LinearSmoothScroller(this.context) {
        override fun getVerticalSnapPreference(): Int = snapMode
        override fun getHorizontalSnapPreference(): Int = snapMode
    }
    smoothScroller.targetPosition = position
    layoutManager?.startSmoothScroll(smoothScroller)
}

fun RecyclerView.attachOnScrollListener(onScrollDown: () -> Unit, onScrollUp: () -> Unit) {
    val scrollListener = RecyclerViewScrollListener(onScrollDown, onScrollUp)
    addOnScrollListener(scrollListener)
}