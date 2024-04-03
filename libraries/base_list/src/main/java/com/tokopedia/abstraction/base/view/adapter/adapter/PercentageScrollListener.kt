package com.tokopedia.abstraction.base.view.adapter.adapter

import android.graphics.Rect
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

open class PercentageScrollListener : OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
    }

    override fun onScrolled(recycler: RecyclerView, dx: Int, dy: Int) {
        val layoutManager = recycler.layoutManager

        val firstPosition = if (layoutManager is StaggeredGridLayoutManager)
            layoutManager.findFirstVisibleItemPositions(null).first()
        else if (layoutManager is GridLayoutManager)
            layoutManager.findFirstVisibleItemPosition()
        else
            (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

        val lastPosition = if (layoutManager is StaggeredGridLayoutManager)
            layoutManager.findLastVisibleItemPositions(null).first()
        else if (layoutManager is GridLayoutManager)
            layoutManager.findLastVisibleItemPosition()
        else
            (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

        val globalVisibleRect = Rect()
        val itemVisibleRect = Rect()

        recycler.getGlobalVisibleRect(globalVisibleRect)

        for (pos in firstPosition..lastPosition) {
            val view = layoutManager.findViewByPosition(pos)
            if (view != null && view.height > 0 && view.getGlobalVisibleRect(itemVisibleRect)) {
                val visibilityExtent = if (itemVisibleRect.bottom >= globalVisibleRect.bottom) {
                    val visibleHeight = globalVisibleRect.bottom - itemVisibleRect.top
                    Math.min(visibleHeight.toFloat() / view.height, 1f)
                } else {
                    val visibleHeight = itemVisibleRect.bottom - globalVisibleRect.top
                    Math.min(visibleHeight.toFloat() / view.height, 1f)
                }
                val viewHolder = recycler.findViewHolderForAdapterPosition(pos) as AbstractViewHolder<*>
                viewHolder.setVisibilityExtent(visibilityExtent)
            }
        }
    }
}
