package com.tokopedia.vouchercreation.common.base

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.listener.EndlessLayoutManagerListener
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster

abstract class BaseSimpleListFragment<T: RecyclerView.Adapter<*>, F>: BaseDaggerFragment() {

    var adapter: T? = null
    private var swipeToRefresh: SwipeRefreshLayout? = null
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null
    private var endlessLayoutManagerListener: EndlessLayoutManagerListener? = null
    private var recyclerView: RecyclerView? = null
    private val recyclerViewLayoutManager
        get() = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    private val isListEmpty
        get() = adapter?.itemCount == 0

    abstract fun createAdapter(): T?
    abstract fun getRecyclerView(view: View): RecyclerView?
    abstract fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout?
    abstract fun addElementToAdapter(list: List<F>)
    abstract fun loadData(page: Int)
    abstract fun clearAdapterData()
    abstract fun onShowLoading()
    abstract fun onHideLoading()
    abstract fun onDataEmpty()
    abstract fun onGetListError(message: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = createAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = getRecyclerView(view)

        var layoutManager = recyclerView?.layoutManager
        if (layoutManager == null) {
            layoutManager = recyclerViewLayoutManager
            recyclerView?.layoutManager = layoutManager
        }

        enableLoadMore()

        swipeToRefresh = getSwipeRefreshLayout(view)
        swipeToRefresh?.setOnRefreshListener { onSwipeRefresh() }
        recyclerView?.adapter = adapter

        loadInitialData()
    }

    private fun onSwipeRefresh() {
        swipeToRefresh?.isRefreshing = true
        loadInitialData()
    }

    private fun loadInitialData() {
        clearAdapterData()
        showLoading()
        loadData(1)
    }

    private fun enableLoadMore() {
        if (endlessRecyclerViewScrollListener == null) {
            endlessRecyclerViewScrollListener = createEndlessRecyclerViewListener()
            endlessRecyclerViewScrollListener?.setEndlessLayoutManagerListener(
                endlessLayoutManagerListener
            )

        }
        endlessRecyclerViewScrollListener?.apply {
            recyclerView?.addOnScrollListener(this)
        }
    }

    private fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerView?.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                showLoading()
                loadData(page)
            }
        }
    }

    private fun updateStateScrollListener() {
        // update the load more state (paging/can loadmore)
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
    }

    private fun onGetListErrorWithEmptyData(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(activity, throwable)
        swipeToRefresh?.isEnabled = false
        onGetListError(message)
    }

    private fun onGetListErrorWithExistingData(throwable: Throwable) {
        showSnackBarRetry(throwable) {
            if (endlessRecyclerViewScrollListener != null) {
                endlessRecyclerViewScrollListener?.loadMoreNextPage()
            } else {
                loadInitialData()
            }
        }
    }

    private fun updateScrollListenerState(hasNextPage: Boolean) {
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(hasNextPage)
    }

    private fun showSwipeLoading() {
        swipeToRefresh?.isRefreshing = true
    }

    private fun showLoading() {
        onShowLoading()
    }

    private fun hideLoading() {
        swipeToRefresh?.isEnabled = true
        swipeToRefresh?.isRefreshing = false
        onHideLoading()
    }

    private fun showSnackBarRetry(throwable: Throwable, listener: View.OnClickListener) {
        val message = ErrorHandler.getErrorMessage(activity, throwable)
        view?.let {
            Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR,
                getString(com.tokopedia.baselist.R.string.retry_label), listener)
        }
    }

    fun renderList(list: List<F>, hasNextPage: Boolean) {
        hideLoading()
        addElementToAdapter(list)

        // update the load more state (paging/can loadmore)
        updateScrollListenerState(hasNextPage)
        if (isListEmpty) {
            onDataEmpty()
        }

        // load next page data if adapter data less than minimum scrollable data
        // when the list has next page and auto load next page is enabled
        if (adapter?.itemCount.orZero() < 10 && hasNextPage && endlessRecyclerViewScrollListener != null) {
            endlessRecyclerViewScrollListener?.loadMoreNextPage()
        }
    }

    fun clearAllData() {
        clearAdapterData()
        endlessRecyclerViewScrollListener?.resetState()
    }

    fun showGetListError(throwable: Throwable) {
        hideLoading()
        updateStateScrollListener()

        if (adapter?.itemCount.orZero().isMoreThanZero()) {
            onGetListErrorWithExistingData(throwable)
        } else {
            onGetListErrorWithEmptyData(throwable)
        }
    }

    fun onRetryClicked() {
        showLoading()
        loadInitialData()
    }
}