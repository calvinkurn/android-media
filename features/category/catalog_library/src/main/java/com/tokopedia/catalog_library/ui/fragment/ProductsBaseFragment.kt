package com.tokopedia.catalog_library.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDataModel
import com.tokopedia.catalog_library.model.util.CatalogLibraryConstant.TOTAL_ROWS_CATALOG
import com.tokopedia.catalog_library.viewmodels.ProductsBaseViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

abstract class ProductsBaseFragment : BaseDaggerFragment() {

    private var linearLayoutManager: LinearLayoutManager? = null
    abstract var baseRecyclerView: RecyclerView?

    private var categoryIdentifier = ""
    private var sortType = 0
    private val rows = TOTAL_ROWS_CATALOG

    private var loadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null

    @JvmField
    @Inject
    var modelFactory: ViewModelProvider.Factory? = null
    private val productsBaseViewModel by lazy {
        modelFactory?.let {
            ViewModelProvider(this, it).get(ProductsBaseViewModel::class.java)
        }
    }

    abstract fun onProductsLoaded(productsList: MutableList<BaseCatalogLibraryDataModel>)
    abstract fun onShimmerAdded()
    abstract fun onErrorFetchingProducts(throwable: Throwable)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayoutManager()
    }

    fun setUpBase() {
        attachScrollListener()
        getProducts()
        setObservers()
    }

    protected fun getLinearLayoutManager(): LinearLayoutManager? {
        return linearLayoutManager
    }

    private fun initLayoutManager() {
        linearLayoutManager = LinearLayoutManager(activity)
    }

    private fun attachScrollListener() {
        getLinearLayoutManager()?.let {
            loadMoreTriggerListener = getEndlessRecyclerViewListener(it)
        }
        loadMoreTriggerListener?.let {
            baseRecyclerView?.addOnScrollListener(it)
        }
    }

    private fun getEndlessRecyclerViewListener(recyclerViewLayoutManager: RecyclerView.LayoutManager): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                productsBaseViewModel?.getCatalogListData(categoryIdentifier, sortType, rows, page)
            }
        }
    }

    private fun getProducts() {
        productsBaseViewModel?.getCatalogListData(categoryIdentifier, sortType, rows)
    }

    private fun setObservers() {
        productsBaseViewModel?.catalogProductsLiveDataResponse?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onProductsLoaded(it.data.listOfComponents)
                    loadMoreTriggerListener?.updateStateAfterGetData()
                }

                is Fail -> {
                    onErrorFetchingProducts(it.throwable)
                }
            }
        }

        productsBaseViewModel?.shimmerLiveData?.observe(viewLifecycleOwner) {
            if (it) {
                onShimmerAdded()
            }
        }
    }
}
