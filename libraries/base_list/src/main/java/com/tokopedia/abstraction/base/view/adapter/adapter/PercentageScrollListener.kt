package com.tokopedia.abstraction.base.view.adapter.adapter

import android.graphics.Rect
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.listener.IAdsViewHolderTrackListener
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

open class PercentageScrollListener : OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        val layoutManager = recyclerView.layoutManager

        val firstPosition = when (layoutManager) {
            is StaggeredGridLayoutManager -> layoutManager.findFirstVisibleItemPositions(null).firstOrNull()
            is GridLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            else -> (layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()
        } ?: return

        val lastPosition = when (layoutManager) {
            is StaggeredGridLayoutManager -> layoutManager.findLastVisibleItemPositions(null).firstOrNull()
            is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
            else -> (layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition()
        } ?: return

        if (firstPosition == RecyclerView.NO_POSITION || lastPosition == RecyclerView.NO_POSITION) {
            return
        }

        val globalVisibleRect = Rect()

        recyclerView.getGlobalVisibleRect(globalVisibleRect)

        for (pos in firstPosition..lastPosition) {
            val view = layoutManager?.findViewByPosition(pos) ?: continue

            val itemVisibleRect = Rect()
            if (view.getGlobalVisibleRect(itemVisibleRect)) {

                val visibleWidth = minOf(itemVisibleRect.right, globalVisibleRect.right) - maxOf(itemVisibleRect.left, globalVisibleRect.left)
                val visibleHeight = minOf(itemVisibleRect.bottom, globalVisibleRect.bottom) - maxOf(itemVisibleRect.top, globalVisibleRect.top)

                val visibleViewArea = visibleWidth * visibleHeight

                // Ensure that the visible area is non-negative
                val visibleArea = max(0, (visibleViewArea))

                val totalArea = view.width * view.height

                val visibleAreaPercentage = if (totalArea > 0) {
                    ((visibleArea.toFloat() / totalArea) * 100).roundToInt()
                } else {
                    0
                }

                val viewHolder = recyclerView.findViewHolderForAdapterPosition(pos) as? IAdsViewHolderTrackListener
                    ?: continue

                val prevValue = viewHolder.visiblePercentage

                viewHolder.setVisiblePercentage(max(prevValue, visibleAreaPercentage))
            }
        }
    }

    override fun onScrolled(recycler: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recycler, dx, dy)
    }
}
