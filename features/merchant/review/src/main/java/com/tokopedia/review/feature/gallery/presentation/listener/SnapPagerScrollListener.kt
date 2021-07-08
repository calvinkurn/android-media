package com.tokopedia.review.feature.gallery.presentation.listener

import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView


class SnapPagerScrollListener(private val snapHelper: PagerSnapHelper, private val listener: ReviewGalleryImageSwipeListener) : RecyclerView.OnScrollListener() {

    companion object {
        const val NOTIFY_ON_INIT = false
    }

    private var snapPosition: Int

    init {
        snapPosition = RecyclerView.NO_POSITION
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (!hasItemPosition()) {
            notifyListenerIfNeeded(getSnapPosition(recyclerView))
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            notifyListenerIfNeeded(getSnapPosition(recyclerView))
        }
    }

    private fun getSnapPosition(recyclerView: RecyclerView): Int {
        val layoutManager: RecyclerView.LayoutManager = recyclerView.layoutManager
                ?: return RecyclerView.NO_POSITION
        val snapView = snapHelper.findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
        return layoutManager.getPosition(snapView)
    }

    private fun notifyListenerIfNeeded(newSnapPosition: Int) {
        if (snapPosition != newSnapPosition) {
            if (NOTIFY_ON_INIT && !hasItemPosition()) {
                listener.onImageSwiped(snapPosition, newSnapPosition)
            } else if (hasItemPosition()) {
                listener.onImageSwiped(snapPosition, newSnapPosition)
            }
            snapPosition = newSnapPosition
        }
    }

    private fun hasItemPosition(): Boolean {
        return snapPosition != RecyclerView.NO_POSITION
    }
}