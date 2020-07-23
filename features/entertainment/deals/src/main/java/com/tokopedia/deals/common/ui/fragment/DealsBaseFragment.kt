package com.tokopedia.deals.common.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.deals.R
import com.tokopedia.deals.common.model.LoadingMoreUnifyModel

/**
 * @author by jessica on 16/06/20
 */

abstract class DealsBaseFragment: BaseDaggerFragment() {

    protected lateinit var adapter: BaseCommonAdapter
    protected lateinit var recyclerView: RecyclerView

    var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null
    private var swipeToRefresh: SwipeRefreshLayout? = null

    protected var isLoadingInitialData = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = createAdapterInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (hasInitialSwipeRefresh()) inflater.inflate(getInitialSwipeLayout(), container, false)
        else inflater.inflate(getInitialLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = getRecyclerView(view)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        if (::adapter.isInitialized) recyclerView.adapter = adapter

        while (recyclerView.itemDecorationCount > 0) {
            recyclerView.removeItemDecorationAt(0)
        }

        if (isLoadMoreEnabled()) enableLoadMore()
        else disableLoadMore()

        swipeToRefresh = getSwipeRefreshLayout(view)

        swipeToRefresh?.let {
            it.setOnRefreshListener { onSwipeRefresh() }
        }

        if (callInitialLoadAutomatically()) {
            loadInitialData()
        }
    }

    open fun getInitialSwipeLayout(): Int = com.tokopedia.baselist.R.layout.fragment_base_list_swipe
    open fun getInitialLayout(): Int = com.tokopedia.baselist.R.layout.fragment_base_list

    protected open fun loadInitialData() {
        isLoadingInitialData = true
        if (::adapter.isInitialized) adapter.clearAllItemsAndAnimateChanges()
        if (hasInitialLoadingModel()) showLoading()
        loadData(DEFAULT_INITIAL_PAGE)
    }

    fun renderList(list: List<Any>, hasNextPage: Boolean) {
        if (hasInitialLoadingModel()) hideLoading()
        swipeToRefresh?.isRefreshing = false

        if (::adapter.isInitialized) {
            if (isLoadingInitialData) {
                adapter.setItemsAndAnimateChanges(list)
            } else {
                hideLoadMoreUnify()
                adapter.hidePageLoad()
                adapter.addItems(list)
            }

            updateScrollListenerState(hasNextPage)

            if (adapter.itemCount == 0) {
                adapter.addItemAndAnimateChanges(EmptyModel())
            } else {
                isLoadingInitialData = false
            }

            if (adapter.itemCount < getMinimumScrollableNumOfItems() && isAutoLoadEnabled()
                    && hasNextPage && isLoadMoreEnabled()) {
                endlessRecyclerViewScrollListener?.apply { loadMoreNextPage() }
            }
        }
    }

    fun clearAllData() {
        if (::adapter.isInitialized) adapter.clearAllItemsAndAnimateChanges()
        endlessRecyclerViewScrollListener?.apply {
            resetState()
        }
    }

    protected open fun hideLoading() {
        if (swipeToRefresh != null) {
            swipeToRefresh!!.isEnabled = true
            swipeToRefresh!!.isRefreshing = false
        }
        if (::adapter.isInitialized) adapter.hideLoading()
    }

    open fun enableLoadMore() {
        if (endlessRecyclerViewScrollListener == null) {
            endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(recyclerView.layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    showLoadMoreLoading()
                    loadData(page)
                }
            }
        }
        endlessRecyclerViewScrollListener?.let {
            recyclerView.addOnScrollListener(it)
        }
    }

    open fun disableLoadMore() {
        endlessRecyclerViewScrollListener?.let {
            recyclerView.removeOnScrollListener(it)
        }
    }

    open fun onSwipeRefresh() {
        swipeToRefresh?.isRefreshing = true
        loadInitialData()
    }

    open fun updateScrollListenerState(hasNextPage: Boolean) {
        endlessRecyclerViewScrollListener?.apply {
            updateStateAfterGetData()
            setHasNextPage(hasNextPage)
        }
    }

    fun showLoading() { if (::adapter.isInitialized) adapter.showPageLoad() }
    fun showLoadMoreLoading() { if (::adapter.isInitialized) showLoadingMoreUnify() }

    open fun getRecyclerView(view: View): RecyclerView = view.findViewById(com.tokopedia.baselist.R.id.recycler_view)
    private fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        return if (hasInitialSwipeRefresh()) view.findViewById(com.tokopedia.baselist.R.id.swipe_refresh_layout)
        else null
    }

    fun showLoadingMoreUnify(){
        adapter.addItem(adapter.data.size, LoadingMoreUnifyModel())
        adapter.notifyItemInserted(adapter.itemCount)
    }

    fun hideLoadMoreUnify(){
        if (adapter.data.isNotEmpty() && adapter.data[adapter.data.lastIndex]::class == LoadingMoreUnifyModel::class) {
            adapter.removeItemAt(adapter.data.lastIndex)
            adapter.notifyItemRemoved(adapter.data.size)
        }
    }

    abstract fun createAdapterInstance(): BaseCommonAdapter
    abstract fun loadData(page: Int)

    open fun hasInitialSwipeRefresh() = false
    open fun hasInitialLoadingModel() = true
    open fun isLoadMoreEnabled() = true
    open fun callInitialLoadAutomatically() = true
    open fun isAutoLoadEnabled() = false
    open fun isShowInitialShimmering() = true
    open fun getMinimumScrollableNumOfItems(): Int = DEFAULT_MAX_ROW_FULL_PAGE

    companion object {
        private const val DEFAULT_INITIAL_PAGE = 1
        private const val DEFAULT_MAX_ROW_FULL_PAGE = 10
    }
}