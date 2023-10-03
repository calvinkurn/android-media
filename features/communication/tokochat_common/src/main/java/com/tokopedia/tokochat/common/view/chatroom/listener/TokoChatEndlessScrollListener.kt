package com.tokopedia.tokochat.common.view.chatroom.listener

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.kotlin.extensions.view.ONE

abstract class TokoChatEndlessScrollListener (
    layoutManager: RecyclerView.LayoutManager
): EndlessRecyclerViewScrollListener(layoutManager) {

    override fun loadMoreNextPage() {
        val totalItemCount = layoutManager.itemCount
        onLoadMore(currentPage + Int.ONE, totalItemCount)
    }

    fun changeLoadingStatus(status: Boolean) {
        loading = status
    }

    fun getLoadingStatus(): Boolean {
        return loading
    }
}
