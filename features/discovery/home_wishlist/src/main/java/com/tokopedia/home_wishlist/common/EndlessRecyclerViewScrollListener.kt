package com.tokopedia.home_wishlist.common

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class EndlessRecyclerViewScrollListener : RecyclerView.OnScrollListener {
    private var visibleThreshold = 2
    private var currentPage: Int = 0
    private var currentItemCount = 0
    private var loading = false
    private var hasNextPage = true
    private var endlessLayoutManagerListener: EndlessLayoutManagerListener? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    constructor(layoutManager: RecyclerView.LayoutManager?, currentPage: Int) {
        layoutManager?.let {
            this.currentPage = currentPage
            resetState()
            this.layoutManager = layoutManager
            if (layoutManager is GridLayoutManager) {
                visibleThreshold = visibleThreshold * layoutManager.spanCount
            } else if (layoutManager is StaggeredGridLayoutManager) {
                visibleThreshold = visibleThreshold * layoutManager.spanCount
            }
        }
    }

    protected fun init() {}
    fun setEndlessLayoutManagerListener(endlessLayoutManagerListener: EndlessLayoutManagerListener?) {
        this.endlessLayoutManagerListener = endlessLayoutManagerListener
    }

    fun getLayoutManager(): RecyclerView.LayoutManager? {
        return if (endlessLayoutManagerListener != null
                && endlessLayoutManagerListener!!.currentLayoutManager != null) endlessLayoutManagerListener!!.currentLayoutManager else layoutManager
    }

    protected fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(view, dx, dy)
        checkLoadMore(view, dx, dy)
    }

    protected fun checkLoadMore(view: RecyclerView?, dx: Int, dy: Int) { // assume load more when going down or going right
        if (dy <= 0 && dx <= 0) {
            return
        }
        if (loading) {
            return
        }
        // no need load more if data is empty
        if (isDataEmpty) {
            return
        }
        val totalItemCount = getLayoutManager()?.itemCount ?: 0
        var lastVisibleItemPosition = 0
        if (getLayoutManager() is StaggeredGridLayoutManager) {
            val lastVisibleItemPositions = (getLayoutManager() as StaggeredGridLayoutManager)
                    .findLastVisibleItemPositions(null)
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
        } else if (getLayoutManager() is GridLayoutManager) {
            lastVisibleItemPosition = (getLayoutManager() as GridLayoutManager).findLastVisibleItemPosition()
        } else if (getLayoutManager() is LinearLayoutManager) {
            lastVisibleItemPosition = (getLayoutManager() as LinearLayoutManager).findLastVisibleItemPosition()
        }
        if (lastVisibleItemPosition + visibleThreshold > totalItemCount &&
                hasNextPage) {
            loadMoreNextPage()
        }
    }

    protected val isDataEmpty: Boolean
        protected get() {
            val totalItemCount = getLayoutManager()?.itemCount ?: 0
            return totalItemCount == 0
        }

    fun loadMoreNextPage() {
        val totalItemCount = getLayoutManager()?.itemCount ?: 0
        loading = true
        onLoadMore(currentPage + 1, totalItemCount)
    }

    fun setHasNextPage(hasNextPage: Boolean) {
        this.hasNextPage = hasNextPage
    }

    fun resetState() {
        currentPage = STARTING_PAGE_INDEX
        currentItemCount = 0
        loading = false
        hasNextPage = true
    }

    fun updateStateAfterGetData() {
        loading = false
        val totalItemCount = getLayoutManager()?.itemCount ?: 0
        if (totalItemCount > currentItemCount) {
            currentItemCount = totalItemCount
            currentPage++
        }
    }

    abstract fun onLoadMore(page: Int, totalItemsCount: Int)

    companion object {
        protected const val STARTING_PAGE_INDEX = 0
    }
}

interface EndlessLayoutManagerListener {
    val currentLayoutManager: RecyclerView.LayoutManager?
}
