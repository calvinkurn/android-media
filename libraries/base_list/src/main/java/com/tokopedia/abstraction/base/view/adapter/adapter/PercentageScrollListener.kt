package com.tokopedia.abstraction.base.view.adapter.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.listener.IAdsViewHolderTrackListener
import kotlin.math.max
import kotlin.math.roundToInt

open class PercentageScrollListener : OnScrollListener() {

    private val globalVisibleRect by lazy { Rect() }
    private val itemVisibleRect by lazy { Rect() }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        setVisiblePercentageOnScrollChanged(recyclerView)
    }

    private fun setVisiblePercentageOnScrollChanged(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager

        val firstPosition = when (layoutManager) {
            is StaggeredGridLayoutManager -> layoutManager.findFirstVisibleItemPositions(null).minOrNull()
            is GridLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            else -> (layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()
        } ?: return

        val lastPosition = when (layoutManager) {
            is StaggeredGridLayoutManager -> layoutManager.findLastVisibleItemPositions(null).maxOrNull()
            is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
            else -> (layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition()
        } ?: return

        if (firstPosition == RecyclerView.NO_POSITION || lastPosition == RecyclerView.NO_POSITION) {
            return
        }

        recyclerView.getGlobalVisibleRect(globalVisibleRect)

        for (pos in firstPosition..lastPosition) {
            setItemViewVisiblePercentage(layoutManager, pos, recyclerView)
        }
    }

    private fun setItemViewVisiblePercentage(layoutManager: RecyclerView.LayoutManager?, i: Int, recyclerView: RecyclerView) {
        val itemView = layoutManager?.findViewByPosition(i) ?: return

        val viewHolder = recyclerView.findViewHolderForAdapterPosition(i) as? IAdsViewHolderTrackListener
            ?: return

        itemView.getGlobalVisibleRect(itemVisibleRect)

        val visibleAreaPercentage = getCalculateVisibleViewArea(itemView, itemVisibleRect, globalVisibleRect)

        val prevValue = viewHolder.visiblePercentage

        viewHolder.setVisiblePercentage(max(prevValue, visibleAreaPercentage))
    }
}


fun getCalculateVisibleViewArea(itemView: View, itemVisibleRect: Rect, globalVisibleRect: Rect): Int {
    val visibleWidth = minOf(itemVisibleRect.right, globalVisibleRect.right) - maxOf(itemVisibleRect.left, globalVisibleRect.left)
    val visibleHeight = minOf(itemVisibleRect.bottom, globalVisibleRect.bottom) - maxOf(itemVisibleRect.top, globalVisibleRect.top)

    val visibleViewArea = visibleWidth * visibleHeight

    // Ensure that the visible area is non-negative
    val visibleArea = max(0, visibleViewArea)

    val totalArea = itemView.width * itemView.height

    val visibleAreaPercentage = if (totalArea > 0) {
        ((visibleArea.toFloat() / totalArea) * 100).roundToInt()
    } else {
        0
    }

    return visibleAreaPercentage
}
