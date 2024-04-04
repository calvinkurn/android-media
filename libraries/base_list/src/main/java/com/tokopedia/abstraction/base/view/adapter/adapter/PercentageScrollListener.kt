package com.tokopedia.abstraction.base.view.adapter.adapter

import android.graphics.Rect
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlin.math.roundToInt

open class PercentageScrollListener : OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
    }

    override fun onScrolled(recycler: RecyclerView, dx: Int, dy: Int) {
        val layoutManager = recycler.layoutManager

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

        val globalVisibleRect = Rect()

        recycler.getGlobalVisibleRect(globalVisibleRect)

        for (pos in firstPosition..lastPosition) {
            val view = layoutManager?.findViewByPosition(pos) ?: continue

            val itemVisibleRect = Rect()
            if (view.getGlobalVisibleRect(itemVisibleRect)) {
                val visibleWidth = minOf(itemVisibleRect.right, globalVisibleRect.right) - maxOf(itemVisibleRect.left, globalVisibleRect.left)
                val visibleHeight = minOf(itemVisibleRect.bottom, globalVisibleRect.bottom) - maxOf(itemVisibleRect.top, globalVisibleRect.top)
                val visibleArea = maxOf(0, visibleWidth) * maxOf(0, visibleHeight)

                val totalArea = view.width * view.height
                val visibleAreaPercentage = ((visibleArea.toFloat() / totalArea) * 100).roundToInt()

                val viewHolder = recycler.findViewHolderForAdapterPosition(pos) as? IAdsViewHolderTrackListener ?: continue
                viewHolder.setVisiblePercentage(visibleAreaPercentage)
            }
        }
    }
}
