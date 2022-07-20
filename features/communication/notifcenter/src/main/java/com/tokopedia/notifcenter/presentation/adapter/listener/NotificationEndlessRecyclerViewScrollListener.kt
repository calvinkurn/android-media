package com.tokopedia.notifcenter.presentation.adapter.listener

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener

class NotificationEndlessRecyclerViewScrollListener(
        private val layoutManager: StaggeredGridLayoutManager,
        private val listener: Listener
) : EndlessRecyclerViewScrollListener(layoutManager) {

    interface Listener {
        fun onLoadMore(page: Int, totalItemsCount: Int)
    }

    override fun checkLoadMore(view: RecyclerView?, dx: Int, dy: Int) {
        if ((dy <= 0 && dx <= 0) || loading || isDataEmpty) return
        val totalItemCount = layoutManager.itemCount
        val lastVisibleItemPositions = layoutManager.findLastVisibleItemPositions(null)
        val lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
        if (lastVisibleItemPosition + visibleThreshold > totalItemCount && hasNextPage) {
            loadMoreNextPage()
        }

        super.checkLoadMore(view, dx, dy)
    }

    override fun onLoadMore(page: Int, totalItemsCount: Int) {
        listener.onLoadMore(page, totalItemsCount)
    }
}