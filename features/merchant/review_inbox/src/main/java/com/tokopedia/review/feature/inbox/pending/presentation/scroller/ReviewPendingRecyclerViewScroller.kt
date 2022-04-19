package com.tokopedia.review.feature.inbox.pending.presentation.scroller

import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

class ReviewPendingRecyclerViewScroller(
    private val recyclerView: RecyclerView
) {
    private val smoothScroller by lazy(LazyThreadSafetyMode.NONE) {
        object : LinearSmoothScroller(recyclerView.context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
    }

    fun scrollToPosition(position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            smoothScroller.targetPosition = position
            recyclerView.layoutManager?.startSmoothScroll(smoothScroller)
        }
    }
}