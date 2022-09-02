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
     * Function to determine whether we should load next page or not
     */
    override fun notifyLoadResult(hasNextPage: Boolean) {
        val config = this.config ?: return

        config.onLoadNextPageFinished()

        scrollListener?.updateStateAfterGetData()
        scrollListener?.setHasNextPage(hasNextPage)
    }

    override fun resetPaging() {
        scrollListener?.resetState()
    }


}