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
//        val itemVisibleRect = Rect()

        recycler.getGlobalVisibleRect(globalVisibleRect)

        for (pos in firstPosition..lastPosition) {
            val view = layoutManager?.findViewByPosition(pos)
//            if (view != null && view.getGlobalVisibleRect(itemVisibleRect)) {
//
//                val viewVisibleSize = view.width * view.height
//
//                val heightVisibility = if (itemVisibleRect.bottom >= globalVisibleRect.bottom) {
//                    val visibleHeight = globalVisibleRect.bottom - itemVisibleRect.top
//                    Math.min(visibleHeight.toFloat() / view.height, 0f)
//                } else {
//                    val visibleHeight = itemVisibleRect.bottom - globalVisibleRect.top
//                    Math.min(visibleHeight.toFloat() / view.height, 0f)
//                }
//
//                val widthVisibility = if (itemVisibleRect.right >= globalVisibleRect.right) {
//                    val visibleWidth = globalVisibleRect.right - itemVisibleRect.left
//                    Math.min(visibleWidth.toFloat() / view.width, 0f)
//                } else {
//                    val visibleWidth = itemVisibleRect.right - globalVisibleRect.left
//                    Math.min(visibleWidth.toFloat() / view.width, 0f)
//                }
//
//                val viewHolder = recycler.findViewHolderForAdapterPosition(pos) as AbstractViewHolder<*>
//                viewHolder.setVisibilityExtent(heightVisibility)
//            }

            if (view != null) {
                val itemVisibleRect = Rect()
                if (view.getGlobalVisibleRect(itemVisibleRect)) {
                    val intersectionRect = Rect()
                    if (intersectionRect.setIntersect(globalVisibleRect, itemVisibleRect)) {
                        val visibleArea = intersectionRect.width() * intersectionRect.height()
                        val totalArea = view.width * view.height
                        val visibleAreaPercentage = ((visibleArea.toFloat() / totalArea) * 100).roundToInt()
                        val viewHolder = recycler.findViewHolderForAdapterPosition(pos) as? IAdsViewHolderTrackListener
                        viewHolder?.setVisiblePercentage(visibleAreaPercentage)
                    }
                }
            }
        }
    }
}
