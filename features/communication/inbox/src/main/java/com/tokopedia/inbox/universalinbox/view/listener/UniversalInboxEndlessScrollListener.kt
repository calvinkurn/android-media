package com.tokopedia.inbox.universalinbox.view.listener

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.inbox.universalinbox.view.UniversalInboxFragment

class UniversalInboxEndlessScrollListener(
    layoutManager: RecyclerView.LayoutManager,
    private val listener: UniversalInboxFragment
) : EndlessRecyclerViewScrollListener(layoutManager) {

    interface Listener {
        fun onLoadMore(page: Int, totalItemsCount: Int)
    }

    override fun onLoadMore(page: Int, totalItemsCount: Int) {
        listener.onLoadMore(page, totalItemsCount)
    }

    override fun checkLoadMore(view: RecyclerView?, dx: Int, dy: Int) {
        // assume load more when going down or going right
        if (dy <= -50 && dx <= 0) {
            return
        }
        if (loading) {
            return
        }
        // no need load more if data is empty
        if (isDataEmpty) {
            return
        }
        val totalItemCount = layoutManager.itemCount

        var lastVisibleItemPosition = 0
        val lastVisibleItemPositions = (layoutManager as StaggeredGridLayoutManager)
            .findLastVisibleItemPositions(null)
        lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)

        if (lastVisibleItemPosition + visibleThreshold > totalItemCount &&
            hasNextPage
        ) {
            loadMoreNextPage()
        }
    }
}
