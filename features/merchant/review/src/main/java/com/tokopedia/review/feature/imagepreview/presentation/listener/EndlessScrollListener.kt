package com.tokopedia.review.feature.imagepreview.presentation.listener

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.review.feature.imagepreview.presentation.adapter.ReviewImagePreviewLayoutManager

class EndlessScrollListener(private val loadMoreListener: ReviewImagePreviewLoadMoreListener, private val layoutManager: ReviewImagePreviewLayoutManager) :
    RecyclerView.OnScrollListener() {

    companion object {
        const val THRESHOLD = 2
    }

    private var loading = false
    private var hasNextPage = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (dx > 0) {
            if (loading) return
            if (!hasNextPage) return
            val totalItemCount = layoutManager.itemCount
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            if (THRESHOLD + lastVisibleItemPosition >= totalItemCount) {
                loadMore()
            }
        }
    }

    fun setHasNextPage(hasNextPage: Boolean) {
        loading = false
        this.hasNextPage = hasNextPage
    }

    private fun loadMore() {
        loading = true
        loadMoreListener.onLoadMore()
    }
}