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

    private val globalVisibleRect = Rect()

    private val LOCK = Any()

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        //horizontal approach
        if (dx > 0 || dx < 0) {
            setVisiblePercentageOnScrollChanged(recyclerView)
        }
        //vertical approach
        else if (dy > 0 || dy < 0) {
            setVisiblePercentageOnScrollChanged(recyclerView)
        }
    }

    private fun setVisiblePercentageOnScrollChanged(recyclerView: RecyclerView) {
        synchronized(LOCK) {
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
                setItemViewVisiblePercentage(layoutManager, pos, recyclerView, globalVisibleRect)
            }
        }
    }

    private fun setItemViewVisiblePercentage(layoutManager: RecyclerView.LayoutManager?, i: Int, recyclerView: RecyclerView, globalVisibleRect: Rect) {
        val itemView = layoutManager?.findViewByPosition(i) ?: return

        val viewHolder = recyclerView.findViewHolderForAdapterPosition(i) as? IAdsViewHolderTrackListener
            ?: return

        val itemVisibleRect = Rect()

        itemView.getGlobalVisibleRect(itemVisibleRect)

        val visibleAreaPercentage = getCalculateVisibleViewArea(itemView, itemVisibleRect, globalVisibleRect)

        val prevValue = viewHolder.visiblePercentage

        viewHolder.setVisiblePercentage(max(prevValue, visibleAreaPercentage))
    }
}

fun getViewAreaPercentage(recyclerView: RecyclerView?, itemView: View?, bindingAdapterPosition: Int): Int {

    if (itemView == null || recyclerView == null) return 0

    val viewHolder = recyclerView.findViewHolderForAdapterPosition(bindingAdapterPosition) as? IAdsViewHolderTrackListener

    val globalVisibleRect = Rect()

    recyclerView.getGlobalVisibleRect(globalVisibleRect)
    val itemVisibleRect = Rect()

    itemView.getGlobalVisibleRect(itemVisibleRect)

    val visibleArea = getCalculateVisibleViewArea(itemView, itemVisibleRect, globalVisibleRect)

    val prevValue = viewHolder?.visiblePercentage ?: 0

    return if (visibleArea > 0) max(prevValue, visibleArea) else 100
}

fun getCalculateVisibleViewArea(itemView: View, itemVisibleRect: Rect, globalVisibleRect: Rect): Int {
    val visibleWidth = minOf(itemVisibleRect.right, globalVisibleRect.right) - maxOf(itemVisibleRect.left, globalVisibleRect.left)
    val visibleHeight = minOf(itemVisibleRect.bottom, globalVisibleRect.bottom) - maxOf(itemVisibleRect.top, globalVisibleRect.top)

    val visibleViewArea = visibleWidth * visibleHeight

    // Ensure that the visible area is non-negative
    val visibleArea = max(0, (visibleViewArea))

    val totalArea = itemView.width * itemView.height

    val visibleAreaPercentage = if (totalArea > 0) {
        ((visibleArea.toFloat() / totalArea) * 100).roundToInt()
    } else {
        0
    }

    return visibleAreaPercentage
}
