package com.tokopedia.catalog_library.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDataModel
import com.tokopedia.catalog_library.viewmodels.ProductsBaseViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

abstract class ProductsBaseFragment : BaseDaggerFragment() {

    private var linearLayoutManager: LinearLayoutManager? = null
    private val categoryId = ""
    private val categoryIdentifier = ""
    private val brandIdentifier = ""
    private val keyword = ""
    private val sortType = "0"
    private val page = ""
    private val rows = ""

    companion object {
        const val DEFAULT_SORT = 23
    }

    @JvmField
    @Inject
    var modelFactory: ViewModelProvider.Factory? = null
    private val productsBaseViewModel by lazy {
        modelFactory?.let {
            ViewModelProvider(this, it).get(ProductsBaseViewModel::class.java)
        }
    }

    abstract fun onProductsLoaded(productsList: MutableList<BaseCatalogLibraryDataModel>)
    abstract fun onErrorFetchingProducts(throwable: Throwable)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayoutManager()
        getProducts()
        setObservers()
    }

    protected fun getLinearLayoutManager(): LinearLayoutManager? {
        return linearLayoutManager
    }

    private fun initLayoutManager() {
        linearLayoutManager = LinearLayoutManager(activity)
    }

    private fun getProducts() {
        productsBaseViewModel?.getCatalogListData(categoryIdentifier, sortType, rows)
    }

    private fun setObservers() {
        productsBaseViewModel?.catalogProductsLiveDataResponse?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onProductsLoaded(it.data.listOfComponents)
                }

                is Fail -> {
                    onErrorFetchingProducts(it.throwable)
                }
            }
        }
    }
}
