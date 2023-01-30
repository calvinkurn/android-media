package com.tokopedia.dilayanitokopedia.home.presentation.adapter

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.listener.EndlessLayoutManagerListener

abstract class HomeFeedEndlessScrollListener(layoutManager: RecyclerView.LayoutManager?) : RecyclerView.OnScrollListener() {
    private var visibleThreshold = 2
    var currentPage = 0
        protected set
    private var currentItemCount = 0
    var loading = false
    private var hasNextPage = true
    private var endlessLayoutManagerListener: EndlessLayoutManagerListener? = null
    private var layoutManager: RecyclerView.LayoutManager? = layoutManager

    companion object {
        protected const val STARTING_PAGE_INDEX = 0
    }

    private val isDataEmpty: Boolean
        get() {
            val totalItemCount = getLayoutManager()?.itemCount
            return totalItemCount == 0
        }

    init {
        resetState()
        resetState()
        if (layoutManager is GridLayoutManager) {
            visibleThreshold *= layoutManager.spanCount
        } else if (layoutManager is StaggeredGridLayoutManager) {
            visibleThreshold *= layoutManager.spanCount
        }
    }


    protected fun init() {}
    fun setEndlessLayoutManagerListener(endlessLayoutManagerListener: EndlessLayoutManagerListener?) {
        this.endlessLayoutManagerListener = endlessLayoutManagerListener
    }

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
        checkLoadMore(view, dx, dy)
    }

    protected fun checkLoadMore(view: RecyclerView?, dx: Int, dy: Int) {

        // assume load more when going down or going right
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
        val totalItemCount = getLayoutManager()?.itemCount
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
        if (lastVisibleItemPosition + visibleThreshold > (totalItemCount ?: 0) && hasNextPage) {
            loadMoreNextPage()
        }
    }


    private fun loadMoreNextPage() {
        val totalItemCount = getLayoutManager()?.itemCount
        loading = true
        onLoadMore(currentPage + 1, totalItemCount ?: 0)
    }


    private fun resetState() {
        currentPage = STARTING_PAGE_INDEX
        currentItemCount = 0
        loading = false
        hasNextPage = true
    }

    fun updateStateAfterGetData() {
        loading = false
        val totalItemCount = getLayoutManager()?.itemCount
        if (totalItemCount != null) {
            if (totalItemCount > currentItemCount) {
                currentItemCount = totalItemCount
                currentPage++
            }
        }
    }

    abstract fun onLoadMore(page: Int, totalItemsCount: Int)

    fun setHasNextPage(hasNextPage: Boolean) {
        this.hasNextPage = hasNextPage
    }

}
