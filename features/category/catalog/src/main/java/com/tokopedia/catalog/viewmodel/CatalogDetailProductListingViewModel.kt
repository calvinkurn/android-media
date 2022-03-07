package com.tokopedia.catalog.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.catalog.adapter.factory.CatalogTypeFactory
import com.tokopedia.catalog.model.raw.CatalogComparisonProductsResponse
import com.tokopedia.catalog.model.raw.CatalogProductItem
import com.tokopedia.catalog.model.raw.ProductListResponse
import com.tokopedia.catalog.ui.fragment.CatalogDetailProductListingFragment
import com.tokopedia.catalog.usecase.listing.CatalogDynamicFilterUseCase
import com.tokopedia.catalog.usecase.listing.CatalogGetProductListUseCase
import com.tokopedia.catalog.usecase.listing.CatalogQuickFilterUseCase
import com.tokopedia.catalog.viewholder.products.CatalogForYouContainerDataModel
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber
import javax.inject.Inject

class CatalogDetailProductListingViewModel
@Inject constructor(private val quickFilterUseCase: CatalogQuickFilterUseCase,
                    private val dynamicFilterUseCase: CatalogDynamicFilterUseCase,
                    private val getProductListUseCase: CatalogGetProductListUseCase) : ViewModel() {

    val mProductList = MutableLiveData<Result<List<CatalogProductItem>>>()
    val mProductCount = MutableLiveData<String>()
    val mQuickFilterModel = MutableLiveData<Result<DynamicFilterModel>>()
    val mDynamicFilterModel = MutableLiveData<Result<DynamicFilterModel>>()

    val selectedSortIndicatorCount = MutableLiveData<Int>()
    val searchParametersMap = MutableLiveData<HashMap<String, String>>()

    var quickFilterOptionList: MutableList<Option> = ArrayList()
    val quickFilterModel = MutableLiveData<DynamicFilterModel>()
    val quickFilterClicked = MutableLiveData<Boolean>()
    val dynamicFilterModel = MutableLiveData<DynamicFilterModel>()

    var filterController: FilterController? = FilterController()
    var searchParameter: SearchParameter = SearchParameter()

    var pageCount = 0
    var isPagingAllowed: Boolean = true
    var catalogUrl = ""
    var catalogName = ""
    var catalogId = ""
    var categoryId = ""
    var brand  = ""
    var comparisonCardIsAdded = false

    val list: ArrayList<Visitable<CatalogTypeFactory>> = ArrayList()

    fun fetchProductListing(params: RequestParams) {
        comparisonCardIsAdded = pageCount != 0
        getProductListUseCase.execute(params, object : Subscriber<ProductListResponse>() {
            override fun onNext(productListResponse: ProductListResponse?) {
                productListResponse?.let { productResponse ->
                    processProductListResponse(productResponse)
                }
                addCatalogForYouCard()
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                addCatalogForYouCard()
                mProductList.value = Fail(e)
            }
        })
    }

    private fun processProductListResponse(productResponse : ProductListResponse){
        (productResponse.searchProduct)?.let { searchProduct ->
            searchProduct.data.catalogProductItemList.let { productList ->
                mProductList.value = Success((productList) as List<CatalogProductItem>)
                list.addAll(productList as ArrayList<Visitable<CatalogTypeFactory>>)
                pageCount++
            }
            mProductCount.value = searchProduct.data.totalData.toString()
        }
    }

    fun fetchQuickFilters(params: RequestParams) {

        quickFilterUseCase.execute(params, object : Subscriber<DynamicFilterModel>() {
            override fun onNext(t: DynamicFilterModel?) {
                mQuickFilterModel.value = Success(t as DynamicFilterModel)
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                mQuickFilterModel.value = Fail(e)
            }
        })
    }

    fun fetchDynamicAttribute(params: RequestParams) {
        dynamicFilterUseCase.execute(params, object : Subscriber<DynamicFilterModel>() {
            override fun onNext(t: DynamicFilterModel?) {
                mDynamicFilterModel.value = Success(t as DynamicFilterModel)
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                mDynamicFilterModel.value = (Fail(e))
            }
        })
    }

    private fun addCatalogForYouCard() {
        if (!comparisonCardIsAdded){
            comparisonCardIsAdded = true
            if(list.size - 1 >= CatalogDetailProductListingFragment.MORE_CATALOG_WIDGET_INDEX){
                list.add(CatalogDetailProductListingFragment.MORE_CATALOG_WIDGET_INDEX,CatalogForYouContainerDataModel() as Visitable<CatalogTypeFactory>)
            }else {
                list.add(CatalogForYouContainerDataModel() as Visitable<CatalogTypeFactory>)
            }
        }
    }

    fun getDynamicFilterData(): MutableLiveData<Result<DynamicFilterModel>> {
        return mDynamicFilterModel
    }

    fun onDetach(){
        dynamicFilterUseCase.unsubscribe()
        quickFilterUseCase.unsubscribe()
        getProductListUseCase.unsubscribe()
    }
}
