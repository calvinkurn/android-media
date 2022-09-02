package com.tokopedia.campaign.delegates

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener

class HasPaginatedListImpl : HasPaginatedList {

    private var config: HasPaginatedList.Config? = null
    private var scrollListener: EndlessRecyclerViewScrollListener? = null

    override fun attachPaging(
        recyclerView: RecyclerView,
        config: HasPaginatedList.Config,
        loadNextPage: (Int, Int) -> Unit
    ) {
        if (recyclerView.adapter == null) throw RuntimeException("RecyclerView adapter must not be null. Please initialize recyclerView adapter first")
        if (recyclerView.layoutManager == null) throw RuntimeException("RecyclerView layout manager must not be null. Please initialize layout manager first")

        this.config = config
        enablePaging(recyclerView, config, loadNextPage)
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