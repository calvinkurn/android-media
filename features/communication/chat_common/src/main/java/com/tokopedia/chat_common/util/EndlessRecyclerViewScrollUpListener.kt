package com.tokopedia.chat_common.util

import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class EndlessRecyclerViewScrollUpListener : EndlessRecyclerViewScrollListener {
    constructor(layoutManager: LinearLayoutManager?) : super(layoutManager) {}
    constructor(layoutManager: GridLayoutManager?) : super(layoutManager) {}
    constructor(layoutManager: StaggeredGridLayoutManager?) : super(layoutManager) {}
    constructor(layoutManager: RecyclerView.LayoutManager?) : super(layoutManager) {}

    override fun checkLoadMore(view: RecyclerView, dx: Int, dy: Int) {
        if (loading) {
            return
        }

        // no need load more if data is empty
        if (isDataEmpty) {
            return
        }
        val totalItemCount = layoutManager.itemCount
        var lastVisibleItemPosition = 0
        when (layoutManager) {
            is StaggeredGridLayoutManager -> {
                val lastVisibleItemPositions = (layoutManager as StaggeredGridLayoutManager)
                    .findLastVisibleItemPositions(null)
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
            }
            is GridLayoutManager -> {
                lastVisibleItemPosition =
                    (layoutManager as GridLayoutManager).findLastVisibleItemPosition()
            }
            is LinearLayoutManager -> {
                lastVisibleItemPosition =
                    (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            }
        }
        if (lastVisibleItemPosition + visibleThreshold > totalItemCount &&
            hasNextPage
        ) {
            loadMoreNextPage()
        }
    }
}