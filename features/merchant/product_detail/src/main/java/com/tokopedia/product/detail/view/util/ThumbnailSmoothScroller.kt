package com.tokopedia.product.detail.view.util

import android.content.Context
import android.graphics.PointF
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

open class ThumbnailSmoothScroller(context: Context,
                                   private val targetRv: RecyclerView)
    : LinearSmoothScroller(context) {

    fun scrollThumbnail(selectedPosition: Int) {
        val thumbnailLayoutManager = targetRv.layoutManager as? LinearLayoutManager
        thumbnailLayoutManager?.let {
            if (selectedPosition >= thumbnailLayoutManager.findLastVisibleItemPosition()) {
                targetPosition = selectedPosition
                thumbnailLayoutManager.startSmoothScroll(
                        this
                )
            } else if (selectedPosition == thumbnailLayoutManager.findFirstVisibleItemPosition()) {
                targetPosition = thumbnailLayoutManager.findFirstVisibleItemPosition() - 1
                targetPosition =
                        if (thumbnailLayoutManager.findFirstVisibleItemPosition() - 1 > 0)
                            thumbnailLayoutManager.findFirstVisibleItemPosition() - 1
                        else 0
                thumbnailLayoutManager.startSmoothScroll(
                        this
                )
            }
        }
    }

    override fun getHorizontalSnapPreference(): Int {
        return SNAP_TO_START
    }

    override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
        return (targetRv.layoutManager as? LinearLayoutManager)?.computeScrollVectorForPosition(
                targetPosition)
    }
}