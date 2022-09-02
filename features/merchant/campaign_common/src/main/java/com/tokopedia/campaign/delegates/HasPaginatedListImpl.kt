package com.tokopedia.campaign.delegates

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.listener.EndlessLayoutManagerListener
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener

class HasPaginatedListImpl : HasPaginatedList {

    private var config: HasPaginatedList.Config? = null
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null
    private var endlessLayoutManagerListener: EndlessLayoutManagerListener? = null

    override fun attachPaging(
        recyclerView: RecyclerView,
        config: HasPaginatedList.Config,
        loadNextPage: (Int, Int) -> Unit
    ) {
        this.config = config
        enablePaging(recyclerView, config, loadNextPage)
        resetPaging()
    }

    private fun enablePaging(recyclerView: RecyclerView, config: HasPaginatedList.Config, loadNextPage: (Int, Int) -> Unit) {
        if (endlessRecyclerViewScrollListener == null) {
            endlessRecyclerViewScrollListener = createEndlessRecyclerViewListener(recyclerView,config, loadNextPage)
            endlessRecyclerViewScrollListener?.setEndlessLayoutManagerListener(
                endlessLayoutManagerListener
            )

        }
        endlessRecyclerViewScrollListener?.apply {
            recyclerView.addOnScrollListener(this)
        }
    }

    private fun createEndlessRecyclerViewListener(recyclerView: RecyclerView, config: HasPaginatedList.Config, loadNextPage: (Int, Int) -> Unit): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerView.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                config.onLoadNextPage()
                loadNextPage(page, totalItemsCount)
            }
        }
    }

    override fun notifyLoadResult(hasNextPage: Boolean) {
        val config = this.config ?: return

        config.onLoadNextPageFinished()

        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(hasNextPage)
    }

    override fun resetPaging() {
        endlessRecyclerViewScrollListener?.resetState()
    }


}