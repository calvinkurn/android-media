package com.tokopedia.catalog.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.catalog.adapter.factory.CatalogTypeFactory
import com.tokopedia.catalog.model.raw.CatalogProductItem
import com.tokopedia.catalog.model.raw.ProductListResponse
import com.tokopedia.catalog.usecase.listing.CatalogDynamicFilterUseCase
import com.tokopedia.catalog.usecase.listing.CatalogGetProductListUseCase
import com.tokopedia.catalog.usecase.listing.CatalogQuickFilterUseCase
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber
import javax.inject.Inject

class CatalogDetailProductListingViewModel
@Inject constructor(var quickFilterUseCase: CatalogQuickFilterUseCase,
                    var dynamicFilterUseCase: CatalogDynamicFilterUseCase,
                    var getProductListUseCase: CatalogGetProductListUseCase) : ViewModel() {

    val mProductList = MutableLiveData<Result<List<CatalogProductItem>>>()
    val mProductCount = MutableLiveData<String>()
    var mQuickFilterModel = MutableLiveData<Result<DynamicFilterModel>>()
    var mDynamicFilterModel = MutableLiveData<Result<DynamicFilterModel>>()

    var sortFilterItems = MutableLiveData<List<SortFilterItem>>()
    var selectedSortIndicatorCount = MutableLiveData<Int>()
    var searchParametersMap = MutableLiveData<HashMap<String, String>>()

    var quickFilterOptionList: List<Option> = ArrayList()
    var dynamicFilterModel = MutableLiveData<DynamicFilterModel>()

    var pageCount = 0
    var isPagingAllowed: Boolean = true

    var list: ArrayList<Visitable<CatalogTypeFactory>> = ArrayList()

    fun fetchProductListing(params: RequestParams) {
        getProductListUseCase.execute(params, object : Subscriber<ProductListResponse>() {
            override fun onNext(productListResponse: ProductListResponse?) {
                productListResponse?.let { productResponse ->
                    (productResponse.searchProduct)?.let { searchProduct ->
                        searchProduct.data.catalogProductItemList.let { productList ->
                            mProductList.value = Success((productList) as List<CatalogProductItem>)
                            list.addAll(productList as ArrayList<Visitable<CatalogTypeFactory>>)
                            pageCount++
                        }
                        mProductCount.value = searchProduct.data.totalData.toString()
                    }
                }

            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {

            }
        })
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

    fun getDynamicFilterData(): MutableLiveData<Result<DynamicFilterModel>> {
        return mDynamicFilterModel
    }

    fun onDetach(){
        dynamicFilterUseCase.unsubscribe()
        quickFilterUseCase.unsubscribe()
        getProductListUseCase.unsubscribe()
    }
}
