package com.tokopedia.campaign.delegates

import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO

class HasPaginatedListImpl : HasPaginatedList {

    private var config: HasPaginatedList.Config? = null
    private var scrollListener: EndlessRecyclerViewScrollListener? = null

    override fun attachPaging(
        recyclerView: RecyclerView,
        config: HasPaginatedList.Config,
        loadNextPage: (Int, Int) -> Unit
    ) {
        this.config = config
        enablePaging(recyclerView, config, loadNextPage)
        resetPaging()
    }

    override fun attachPagingWithNestedScrollView(
        nestedScrollView: NestedScrollView,
        config: HasPaginatedList.Config,
        loadNextPage: () -> Unit
    ) {
        this.config = config
        enableNestedScrollViewPaging(nestedScrollView, config, loadNextPage)
        resetPaging()
    }

    private fun enablePaging(
        recyclerView: RecyclerView,
        config: HasPaginatedList.Config,
        loadNextPage: (Int, Int) -> Unit
    ) {
        scrollListener = object : EndlessRecyclerViewScrollListener(recyclerView.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                config.onLoadNextPage()
                loadNextPage(page, totalItemsCount)
            }
        }
        recyclerView.addOnScrollListener(scrollListener ?: return)
    }

    /**
     * Listen to NestedScrollView scroll state
     * loadNextPage does not pass any param such as offset since the data can't be provided
     * use the adapter item size instead as the offset
     */
    private fun enableNestedScrollViewPaging(
        nestedScrollView: NestedScrollView,
        config: HasPaginatedList.Config,
        loadNextPage: () -> Unit
    ) {
        nestedScrollView.apply {
            viewTreeObserver.addOnScrollChangedListener {
                val scrollState: Int =
                    this.getChildAt(this.childCount - Int.ONE).bottom - (this.height + this.scrollY)
                if (scrollState == Int.ZERO) {
                    config.onLoadNextPage
                    loadNextPage()
                }
            }
        }
    }

    /**
     * Function to notify whether recyclerview should load next page or not
     * sample usage: call this function when you've finished loaded the first page data
     */
    override fun notifyLoadResult(hasNextPage: Boolean) {
        config?.run {
            onLoadNextPageFinished()
        }

        scrollListener?.updateStateAfterGetData()
        scrollListener?.setHasNextPage(hasNextPage)
    }

    /**
     * To reset scroll listener state.
     * sample usage: call this function when you apply filter or sort operation
     */
    override fun resetPaging() {
        scrollListener?.resetState()
    }
}