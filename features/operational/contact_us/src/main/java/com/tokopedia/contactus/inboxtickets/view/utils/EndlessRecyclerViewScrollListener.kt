package com.tokopedia.contactus.inboxtickets.view.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.listener.EndlessLayoutManagerListener
import com.tokopedia.kotlin.extensions.view.orZero

abstract class EndlessRecyclerViewScrollListener(private var layoutManager: RecyclerView.LayoutManager) :
    RecyclerView.OnScrollListener() {
    private var visibleThreshold = 2
    var currentPage = 0
        protected set
    private var currentItemCount = 0
    private var loading = false
    private var hasNextPage = true
    private var endlessLayoutManagerListener: EndlessLayoutManagerListener? = null

    protected fun init() {}

    private fun getLayoutManager(): RecyclerView.LayoutManager? {
        return if (endlessLayoutManagerListener != null
            && endlessLayoutManagerListener?.currentLayoutManager != null
        ) endlessLayoutManagerListener?.currentLayoutManager else layoutManager
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
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
        checkLoadMore(dx, dy)
    }

    private fun checkLoadMore(dx: Int, dy: Int) {

        // assume load more when going down or going right
        if (dy <= 0 && dx <= 0 && !hasNextPage) {
            return
        }

        if (loading) {
            return
        }
        // no need load more if data is empty
        if (isDataEmpty) {
            return
        }
        val totalItemCount = getLayoutManager()?.itemCount
        var lastVisibleItemPosition = 0
        if (getLayoutManager() is StaggeredGridLayoutManager) {
            val lastVisibleItemPositions = (getLayoutManager() as StaggeredGridLayoutManager)
                .findLastVisibleItemPositions(null)
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
        } else if (getLayoutManager() is GridLayoutManager) {
            lastVisibleItemPosition =
                (getLayoutManager() as GridLayoutManager).findLastVisibleItemPosition()
        } else if (getLayoutManager() is LinearLayoutManager) {
            lastVisibleItemPosition =
                (getLayoutManager() as LinearLayoutManager).findLastVisibleItemPosition()
        }
        if (lastVisibleItemPosition + visibleThreshold > totalItemCount.orZero() &&
            hasNextPage
        ) {
            loadMoreNextPage()
        }
    }

    private val isDataEmpty: Boolean
        get() {
            val totalItemCount = getLayoutManager()?.itemCount
            return totalItemCount == 0
        }

    private fun loadMoreNextPage() {
        val totalItemCount = getLayoutManager()?.itemCount
        loading = true
        onLoadMore(currentPage + 1, totalItemCount.orZero())
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
        val totalItemCount = getLayoutManager()?.itemCount.orZero()
        if (totalItemCount > currentItemCount) {
            currentItemCount = totalItemCount
            currentPage++
        }
    }

    abstract fun onLoadMore(page: Int, totalItemsCount: Int)

    companion object {
        protected const val STARTING_PAGE_INDEX = 0
    }

    init {
        resetState()
        if (layoutManager is GridLayoutManager) {
            visibleThreshold *= (layoutManager as GridLayoutManager).spanCount
        } else if (layoutManager is StaggeredGridLayoutManager) {
            visibleThreshold *= (layoutManager as StaggeredGridLayoutManager).spanCount
        }
    }
}
