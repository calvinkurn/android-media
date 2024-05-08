package com.tokopedia.abstraction.base.view.adapter.adapter

import android.graphics.Rect
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.listener.IAdsViewHolderTrackListener
import kotlin.math.max
import kotlin.math.roundToInt


open class PercentageScrollListener : OnScrollListener() {

    private val itemVisibleRect by lazy { Rect() }
    private val globalVisibleRect by lazy { Rect() }
    private val set = ConstraintSet()

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        setVisiblePercentageOnScrollChanged(recyclerView)
    }

    private fun setVisiblePercentageOnScrollChanged(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager ?: return

        val firstPosition = when (layoutManager) {
            is StaggeredGridLayoutManager -> {
                val firstVisiblePosition = IntArray(layoutManager.spanCount)
                layoutManager.findFirstVisibleItemPositions(firstVisiblePosition).minOrNull()
            }

            is GridLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            else -> (layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()
        } ?: return

        val lastPosition = when (layoutManager) {
            is StaggeredGridLayoutManager -> {
                val lastVisiblePosition = IntArray(layoutManager.spanCount)
                layoutManager.findLastVisibleItemPositions(lastVisiblePosition).maxOrNull()
            }

            is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
            else -> (layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition()
        } ?: return

        if (firstPosition == RecyclerView.NO_POSITION || lastPosition == RecyclerView.NO_POSITION) {
            return
        }

        val totalLastPosition = lastPosition + (layoutManager.itemCount - lastPosition)

        for (pos in firstPosition..totalLastPosition) {
            setItemViewVisiblePercentage(pos, layoutManager, recyclerView)
        }
    }

    private fun setItemViewVisiblePercentage(i: Int, layoutManager: RecyclerView.LayoutManager?, recyclerView: RecyclerView) {
        val itemView = layoutManager?.findViewByPosition(i) ?: return
        val adsViewHolder = recyclerView.findViewHolderForAdapterPosition(i) as? IAdsViewHolderTrackListener
            ?: return

        val maxPercentageResult = getCalculateVisibleViewArea(itemView, itemVisibleRect, adsViewHolder.visiblePercentage)

        adsViewHolder.setVisiblePercentage(maxPercentageResult)

    }
}

fun getCalculateVisibleViewArea(itemView: View, itemVisibleRect: Rect, prevPercentage: Int): Int {

    itemView.getGlobalVisibleRect(itemVisibleRect)

    val visibleWidth = itemVisibleRect.right - itemVisibleRect.left
    val visibleHeight = itemVisibleRect.bottom - itemVisibleRect.top

    val visibleViewArea = visibleWidth * visibleHeight

    // Ensure that the visible area is non-negative
    val visibleArea = max(0, visibleViewArea)

    val totalArea = itemView.width * itemView.height

    val visibleAreaPercentage = if (totalArea > 0) {
        ((visibleArea.toFloat() / totalArea) * 100).roundToInt()
    } else {
        0
    }

    return max(visibleAreaPercentage, prevPercentage)
}

