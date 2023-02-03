package com.tokopedia.product.detail.view.util

import android.content.Context
import android.graphics.PointF
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

open class ThumbnailSmoothScroller(
    context: Context,
    private val targetRv: RecyclerView
) : LinearSmoothScroller(context) {

    fun scrollThumbnail(selectedPosition: Int) {
        val thumbnailLayoutManager = targetRv.layoutManager as? LinearLayoutManager
        thumbnailLayoutManager?.let {
            val lastVisibleItem = thumbnailLayoutManager.findLastVisibleItemPosition()
            val firstVisibleItem = thumbnailLayoutManager.findFirstVisibleItemPosition()
            if (selectedPosition >= lastVisibleItem) {
                targetPosition = selectedPosition
                thumbnailLayoutManager.startSmoothScroll(this)
            } else if (selectedPosition <= firstVisibleItem) {
                targetPosition = selectedPosition - 1
                targetPosition = if (selectedPosition - 1 > 0) selectedPosition - 1 else 0
                thumbnailLayoutManager.startSmoothScroll(this)
            }
        }
    }

    override fun getHorizontalSnapPreference(): Int {
        return SNAP_TO_START
    }

    override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
        return (targetRv.layoutManager as? LinearLayoutManager)?.computeScrollVectorForPosition(
            targetPosition
        )
    }
}
