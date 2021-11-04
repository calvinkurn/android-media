package com.tokopedia.play.broadcaster.util.scroll

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Created by jegul on 03/06/20
 */
abstract class EndlessRecyclerViewScrollListener(
        private val layoutManager: RecyclerView.LayoutManager
) : RecyclerView.OnScrollListener() {

    private val threshold = 2
    private val visibleThreshold: Int = when (layoutManager) {
        is GridLayoutManager -> threshold * layoutManager.spanCount
        is StaggeredGridLayoutManager -> threshold * layoutManager.spanCount
        is LinearLayoutManager -> threshold
        else -> threshold
    }
    private var currentPage: Int = STARTING_PAGE_INDEX
    private var currentItemCount = 0
    private var loading = false
    private var hasNextPage = true

    protected fun init() {}

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
        val totalItemCount = layoutManager.itemCount
        var lastVisibleItemPosition = 0
        lastVisibleItemPosition = when (layoutManager) {
            is StaggeredGridLayoutManager -> {
                val lastVisibleItemPositions = layoutManager.findLastVisibleItemPositions(null)
                getLastVisibleItem(lastVisibleItemPositions)
            }
            is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
            is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
            else -> throw IllegalStateException("Layout Manager not recognized")
        }
        if (lastVisibleItemPosition + visibleThreshold > totalItemCount &&
                hasNextPage) {
            loadMoreNextPage()
        }
    }

    protected val isDataEmpty: Boolean
        get() {
            val totalItemCount = layoutManager.itemCount
            return totalItemCount == 0
        }

    fun loadMoreNextPage() {
        val totalItemCount = layoutManager.itemCount
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

    fun updateState(isSuccess: Boolean) {
        loading = false
        val totalItemCount = layoutManager.itemCount
        if (totalItemCount > currentItemCount) {
            currentItemCount = totalItemCount
            if (isSuccess) currentPage++
        }
    }

    abstract fun onLoadMore(page: Int, totalItemsCount: Int)

    companion object {
        protected const val STARTING_PAGE_INDEX = 0
    }
}