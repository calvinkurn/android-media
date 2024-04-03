package com.tokopedia.inbox.universalinbox.view.listener

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener

class UniversalInboxEndlessScrollListener(
    layoutManager: RecyclerView.LayoutManager,
    private val listener: Listener
) : EndlessRecyclerViewScrollListener(layoutManager) {

    interface Listener {
        fun onLoadMore(page: Int, totalItemsCount: Int)
    }

    override fun onLoadMore(page: Int, totalItemsCount: Int) {
        listener.onLoadMore(page, totalItemsCount)
    }
}
