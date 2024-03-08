package com.tokopedia.catalog.base

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener

open class CatalogEndlessRecyclerViewScrollListener(layoutManagerListener: RecyclerView.LayoutManager?) : EndlessRecyclerViewScrollListener(layoutManagerListener) {

    override fun updateStateAfterGetData() {
        loading = false
        val totalItemCount = layoutManager.itemCount
        if (hasNextPage) {
            currentItemCount = totalItemCount
            currentPage++
        }
    }
    fun setInitialPage(page: Int){
        this.currentPage = page
    }
    override fun onLoadMore(page: Int, totalItemsCount: Int) {

    }
}
