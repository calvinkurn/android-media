package com.tokopedia.tokochat_common.view.listener

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener

abstract class TokoChatEndlessScrollListener (
    layoutManager: RecyclerView.LayoutManager
): EndlessRecyclerViewScrollListener(layoutManager) {

    override fun loadMoreNextPage() {
        val totalItemCount = layoutManager.itemCount
        onLoadMore(currentPage + 1, totalItemCount)
    }

    fun changeLoadingStatus(status: Boolean) {
        loading = status
    }

    fun getLoadingStatus(): Boolean {
        return loading
    }
}
