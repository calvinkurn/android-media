package com.tokopedia.catalog_library.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDM
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.SORT_TYPE_CATALOG
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.TOTAL_ROWS_CATALOG
import com.tokopedia.catalog_library.viewmodel.CatalogProductsBaseViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

abstract class CatalogProductsBaseFragment : CatalogLibraryBaseFragment() {

    private var categoryId = ""
    private var brandId = ""
    protected var productCount = 0
    private var sortType = SORT_TYPE_CATALOG
    private val rows = TOTAL_ROWS_CATALOG
    private var pageNumber = 1
    private var loadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null
    private var linearLayoutManager: LinearLayoutManager? = null

    abstract var baseRecyclerView: RecyclerView?
    abstract var source: String

    @JvmField
    @Inject
    var modelFactory: ViewModelProvider.Factory? = null
    private val productsBaseVM by lazy {
        modelFactory?.let {
            ViewModelProvider(this, it).get(CatalogProductsBaseViewModel::class.java)
        }
    }

    abstract fun onProductsLoaded(productsList: MutableList<BaseCatalogLibraryDM>)
    abstract fun onShimmerAdded()
    abstract fun onErrorFetchingProducts(throwable: Throwable)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayoutManager()
    }

    fun setCategory(categoryIdStr: String) {
        categoryId = categoryIdStr
    }

    fun setBrandId(brandId: String) {
        this.brandId = brandId
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
                productsBaseVM?.getCatalogListData(source, categoryId, sortType, rows, pageNumber, brandId)
            }
        }
    }

    fun getProducts() {
        resetPage()
        productsBaseVM?.getCatalogListData(source, categoryId, sortType, rows, page = 1, brandId = brandId)
    }

    private fun resetPage() {
        pageNumber = 1
        loadMoreTriggerListener?.resetState()
    }

    private fun setObservers() {
        productsBaseVM?.catalogProductsLiveDataResponse?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onProductsLoaded(it.data.listOfComponents)
                    productCount += it.data.listOfComponents.size
                    pageNumber += 1
                    loadMoreTriggerListener?.updateStateAfterGetData()
                }

                is Fail -> {
                    onErrorFetchingProducts(it.throwable)
                }
            }
        }

        productsBaseVM?.shimmerLiveData?.observe(viewLifecycleOwner) {
            if (it) {
                onShimmerAdded()
            }
        }
    }
}
