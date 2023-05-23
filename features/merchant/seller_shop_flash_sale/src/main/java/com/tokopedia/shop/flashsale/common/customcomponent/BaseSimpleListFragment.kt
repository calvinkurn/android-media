package com.tokopedia.shop.flashsale.common.customcomponent

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.listener.EndlessLayoutManagerListener
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster

abstract class BaseSimpleListFragment<T: RecyclerView.Adapter<*>, F>: BaseDaggerFragment() {

    companion object {
        private const val DEFAULT_FIRST_PAGE = 1
    }

    var adapter: T? = null
    var recyclerView: RecyclerView? = null
    var swipeToRefresh: SwipeRefreshLayout? = null
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null
    private var endlessLayoutManagerListener: EndlessLayoutManagerListener? = null
    private val recyclerViewLayoutManager
        get() = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    private val isListEmpty
        get() = adapter?.itemCount.isZero()

    abstract fun createAdapter(): T?
    abstract fun getRecyclerView(view: View): RecyclerView?
    abstract fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout?
    abstract fun getPerPage(): Int
    abstract fun addElementToAdapter(list: List<F>)
    abstract fun loadData(page: Int)
    abstract fun clearAdapterData()
    abstract fun onShowLoading()
    abstract fun onHideLoading()
    abstract fun onDataEmpty()
    abstract fun onGetListError(message: String)
    abstract fun onScrolled(xScrollAmount: Int, yScrollAmount: Int)

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
        setupSwipeToRefreshLayout(view)
        recyclerView?.adapter = adapter
        loadInitialData()
    }

    private fun setupSwipeToRefreshLayout(view: View) {
        val loaderColor = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        swipeToRefresh = getSwipeRefreshLayout(view)
        swipeToRefresh?.setColorSchemeColors(loaderColor)
        swipeToRefresh?.setOnRefreshListener { onSwipeRefresh() }
    }

    private fun onSwipeRefresh() {
        swipeToRefresh?.isRefreshing = true
        loadInitialData()
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
            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)
                onScrolled(dx, dy)
            }

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
        recyclerView?.gone()
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
                getString(com.tokopedia.baselist.R.string.retry_label), listener).show()
        }
    }

    fun loadInitialData() {
        clearAllData()
        showLoading()
        loadData(DEFAULT_FIRST_PAGE)
    }

    fun renderList(list: List<F>, hasNextPage: Boolean) {
        hideLoading()
        addElementToAdapter(list)

        // update the load more state (paging/can loadmore)
        updateScrollListenerState(hasNextPage)
        if (isListEmpty) {
            onDataEmpty()
            recyclerView?.gone()
        } else {
            recyclerView?.show()
        }

        // load next page data if adapter data less than minimum scrollable data
        // when the list has next page and auto load next page is enabled
        if (adapter?.itemCount.orZero() < getPerPage() && hasNextPage && endlessRecyclerViewScrollListener != null) {
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
}