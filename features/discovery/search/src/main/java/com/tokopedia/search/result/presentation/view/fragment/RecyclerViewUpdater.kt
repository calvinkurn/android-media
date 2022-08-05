package com.tokopedia.search.result.presentation.view.fragment

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.result.presentation.view.adapter.ProductListAdapter
import com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration.ProductItemDecoration
import com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration.SeparatorItemDecoration
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.ViewUpdater
import com.tokopedia.search.result.product.performancemonitoring.PerformanceMonitoringProvider
import com.tokopedia.search.result.product.performancemonitoring.stopPerformanceMonitoring
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import javax.inject.Inject

class RecyclerViewUpdater @Inject constructor(
    performanceMonitoringProvider: PerformanceMonitoringProvider,
    @SearchContext
    context: Context,
) : ViewUpdater,
    ProductListAdapter.OnItemChangeView,
    ContextProvider by WeakReferenceContextProvider(context) {

    var recyclerView: RecyclerView? = null
        private set
    var productListAdapter: ProductListAdapter? = null
        private set

    private val performanceMonitoring = performanceMonitoringProvider.get()

    fun initialize(
        recyclerView: RecyclerView?,
        rvLayoutManager: RecyclerView.LayoutManager?,
        onScrollListener: RecyclerView.OnScrollListener?,
        productListTypeFactory: ProductListTypeFactory,
    ) {
        this.recyclerView = recyclerView
        this.productListAdapter = ProductListAdapter(
            itemChangeView = this,
            typeFactory = productListTypeFactory
        )

        setupRecyclerView(rvLayoutManager, onScrollListener)
    }

    private fun setupRecyclerView(
        rvLayoutManager: RecyclerView.LayoutManager?,
        rvOnScrollListener: RecyclerView.OnScrollListener?,
    ) {
        rvLayoutManager ?: return
        rvOnScrollListener ?: return

        this.recyclerView?.run {
            layoutManager = rvLayoutManager
            adapter = productListAdapter
            addItemDecoration(createProductItemDecoration())
            addItemDecoration(SeparatorItemDecoration(context, productListAdapter))
            addOnScrollListener(rvOnScrollListener)
        }
    }

    private fun createProductItemDecoration(): ProductItemDecoration =
        ProductItemDecoration(getSpacing())

    private fun getSpacing(): Int =
        context
            ?.resources
            ?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)
            ?: 0

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

    override fun onChangeList() {
        recyclerView?.requestLayout()
    }

    override fun onChangeDoubleGrid() {
        recyclerView?.requestLayout()
    }

    override fun onChangeSingleGrid() {
        recyclerView?.requestLayout()
    }
}
