package com.tokopedia.search.result.presentation.view.fragment

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.presentation.view.adapter.ProductListAdapter
import com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration.ProductItemDecoration
import com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration.ProductListViewItemDecoration
import com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration.SeparatorItemDecoration
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationListener
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.ViewUpdater
import com.tokopedia.search.result.product.performancemonitoring.PerformanceMonitoringProvider
import com.tokopedia.search.result.product.performancemonitoring.stopPerformanceMonitoring
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import javax.inject.Inject

@SearchScope
class RecyclerViewUpdater @Inject constructor(
    private val searchNavigationListener: SearchNavigationListener?,
    performanceMonitoringProvider: PerformanceMonitoringProvider,
    @SearchContext
    context: Context,
) : ViewUpdater,
    LifecycleObserver,
    ContextProvider by WeakReferenceContextProvider(context) {

    var recyclerView: RecyclerView? = null
        private set
    var productListAdapter: ProductListAdapter? = null
        private set

    private val performanceMonitoring = performanceMonitoringProvider.get()

    override val itemCount: Int
        get() = productListAdapter?.itemCount ?: 0

    override val itemList: List<Visitable<*>>?
        get() = productListAdapter?.itemList

    fun initialize(
        recyclerView: RecyclerView?,
        rvLayoutManager: RecyclerView.LayoutManager?,
        onScrollListenerList: List<RecyclerView.OnScrollListener?>,
        productListTypeFactory: ProductListTypeFactory,
        viewLifecycleOwner: LifecycleOwner,
    ) {
        this.recyclerView = recyclerView
        this.productListAdapter = ProductListAdapter(productListTypeFactory)

        setupRecyclerView(rvLayoutManager, onScrollListenerList)

        registerLifecycleObserver(viewLifecycleOwner)
    }

    fun changeLayoutManager(
        layoutManager: RecyclerView.LayoutManager,
        removedScrollListeners: List<RecyclerView.OnScrollListener?>,
        addedScrollListeners: List<RecyclerView.OnScrollListener?>,
    ) {
        recyclerView?.apply {
            this.layoutManager = layoutManager
            removedScrollListeners.filterNotNull().forEach(::removeOnScrollListener)
            addedScrollListeners.filterNotNull().forEach(::addOnScrollListener)
        }
    }

    private fun setupRecyclerView(
        rvLayoutManager: RecyclerView.LayoutManager?,
        onScrollListenerList: List<RecyclerView.OnScrollListener?>,
    ) {
        rvLayoutManager ?: return

        this.recyclerView?.run {
            layoutManager = rvLayoutManager
            adapter = productListAdapter
            addItemDecoration(ProductItemDecoration(getSpacing(), productListAdapter))
            addItemDecoration(ProductListViewItemDecoration(context, productListAdapter))
            onScrollListenerList.filterNotNull().forEach(::addOnScrollListener)
        }
    }

    private fun getSpacing(): Int =
        context
            ?.resources
            ?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)
            ?: 0

    override fun getItemAtIndex(index: Int): Visitable<*>? {
        val itemList = productListAdapter?.itemList ?: return null
        if (index !in itemList.indices) return null
        if (itemList.size < index) return null
        return itemList.getOrNull(index)
    }

    private fun registerLifecycleObserver(viewLifecycleOwner: LifecycleOwner) {
        viewLifecycleOwner.lifecycle.addObserver(this)
    }

    override fun setItems(list: List<Visitable<*>>) {
        productListAdapter?.clearData()

        stopPerformanceMonitoring(performanceMonitoring, recyclerView)
        appendItems(list)
    }

    override fun appendItems(list: List<Visitable<*>>) {
        productListAdapter?.appendItems(list)
    }

    override fun refreshItemAtIndex(index: Int) {
        productListAdapter?.refreshItemAtIndex(index)
    }

    override fun addLoading() {
        productListAdapter?.addLoading()
    }

    override fun removeLoading() {
        productListAdapter?.removeLoading()
    }

    override fun requestRelayout() {
        recyclerView?.requestLayout()
    }

    override fun removeFirstItemWithCondition(condition: (Visitable<*>) -> Boolean) {
        productListAdapter?.removeFirstItemWithCondition(condition)
    }

    override fun insertItemAfter(item: Visitable<*>, previousItem: Visitable<*>) {
        productListAdapter?.insertItemAfter(item, previousItem)
    }

    override fun backToTop() {
        recyclerView?.smoothScrollToPosition(0)
    }

    override fun scrollToPosition(position: Int) {
        recyclerView?.scrollToPosition(position)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        recyclerView = null
        productListAdapter = null
    }
}
