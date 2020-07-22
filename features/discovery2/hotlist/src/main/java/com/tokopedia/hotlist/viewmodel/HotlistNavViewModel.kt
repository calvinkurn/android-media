package com.tokopedia.hotlist.viewmodel

import androidx.lifecycle.*
import com.tokopedia.common_category.model.productModel.ProductListResponse
import com.tokopedia.common_category.model.productModel.ProductsItem
import com.tokopedia.common_category.usecase.*
import com.tokopedia.hotlist.data.cpmAds.CpmItem
import com.tokopedia.hotlist.data.hotListDetail.HotListDetailResponse
import com.tokopedia.hotlist.domain.usecases.CpmAdsUseCase
import com.tokopedia.hotlist.domain.usecases.HotlistDetailUseCase
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber
import javax.inject.Inject

class HotlistNavViewModel @Inject constructor(private var hotlistDetailUseCase: HotlistDetailUseCase,
                                              private var getProductListUseCase: GetProductListUseCase,
                                              private var categoryProductUseCase: CategoryProductUseCase,
                                              private var quickFilterUseCase: QuickFilterUseCase,
                                              private var dynamicFilterUseCase: DynamicFilterUseCase,
                                              private var cpmAdsUseCase: CpmAdsUseCase,
                                              var sendTopAdsUseCase: SendTopAdsUseCase) : ViewModel(), LifecycleObserver {

    val mHotListDetailResponse = MutableLiveData<Result<HotListDetailResponse>>()
    val mProductList = MutableLiveData<Result<List<ProductsItem>>>()
    val mProductCount = MutableLiveData<String>()
    var mDynamicFilterModel = MutableLiveData<Result<DynamicFilterModel>>()
    var mQuickFilterModel = MutableLiveData<Result<List<Filter>>>()
    var mCpmTopAdsData = MutableLiveData<Result<List<CpmItem>>>()


    fun getHotlistDetail(params: RequestParams) {
        hotlistDetailUseCase.execute(params, object : Subscriber<HotListDetailResponse>() {
            override fun onNext(hotlistRes: HotListDetailResponse?) {
                hotlistRes?.let {
                    mHotListDetailResponse.value = Success(it)
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                mHotListDetailResponse.value = Fail(e ?: Throwable("Error Thrown"))
            }
        })
    }


    fun fetchProductListingWithTopAds(params: RequestParams) {
        getProductListUseCase.execute(params, object : Subscriber<ProductListResponse>() {
            override fun onNext(productListResponse: ProductListResponse?) {
                productListResponse?.let { productResponse ->
                    (productResponse.searchProduct)?.let { searchProduct ->
                        searchProduct.products.let { productList ->
                            mProductList.value = Success((productList) as List<ProductsItem>)
                        }

                        mProductCount.value = searchProduct.countText
                    }
                }

            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                mProductList.value = Fail(e ?: Throwable("Throw Error"))
            }
        })
    }

    fun getProductListWithoutTopAds(params: RequestParams) {
        categoryProductUseCase.execute(params, object : Subscriber<ProductListResponse>() {
            override fun onNext(productListResponse: ProductListResponse?) {
                productListResponse?.let { productResponse ->
                    (productResponse.searchProduct)?.let { searchProduct ->
                        searchProduct.products?.let { productList ->
                            mProductList.value = Success((productList) as List<ProductsItem>)
                        }

                        mProductCount.value = searchProduct.countText
                    }
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                mProductList.value = Fail(e ?: Throwable("Throw Error"))
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

    fun fetchQuickFilters(params: RequestParams) {

        quickFilterUseCase.execute(params, object : Subscriber<List<Filter>>() {
            override fun onNext(t: List<Filter>?) {
                mQuickFilterModel.value = Success(t as List<Filter>)
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                mQuickFilterModel.value = Fail(e)
            }
        })
    }

    fun fetchCpmData(params: RequestParams) {
        cpmAdsUseCase.execute(params, object : Subscriber<List<CpmItem>>() {
            override fun onNext(t: List<CpmItem>?) {
                mCpmTopAdsData.value = Success(t as List<CpmItem>)
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                mCpmTopAdsData.value = Fail(e)
            }
        })
    }

    fun sendTopAdsImpressions(url: String, id: String, name: String, imageURL: String) {
        sendTopAdsUseCase.hitImpressions(url, id, name, imageURL)
    }

    fun sendTopAdsClick(url: String, id: String, name: String, imageURL: String) {
        sendTopAdsUseCase.hitClick(url, id, name, imageURL)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun onDestroy() {
        hotlistDetailUseCase.unsubscribe()
        getProductListUseCase.unsubscribe()
        categoryProductUseCase.unsubscribe()
        quickFilterUseCase.unsubscribe()
        dynamicFilterUseCase.unsubscribe()
        cpmAdsUseCase.unsubscribe()
    }
}