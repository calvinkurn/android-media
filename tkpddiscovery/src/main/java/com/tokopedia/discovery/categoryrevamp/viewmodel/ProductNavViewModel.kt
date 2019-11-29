package com.tokopedia.discovery.categoryrevamp.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import com.tokopedia.discovery.categoryrevamp.data.bannedCategory.Data
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductListResponse
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductsItem
import com.tokopedia.discovery.categoryrevamp.data.subCategoryModel.SubCategoryItem
import com.tokopedia.discovery.categoryrevamp.domain.usecase.*
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.seamless_login.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.seamless_login.subscriber.SeamlessLoginSubscriber
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber
import javax.inject.Inject

class ProductNavViewModel @Inject constructor(var subCategoryUseCaseV3: SubCategoryV3UseCase,
                                              var dynamicFilterUseCase: DynamicFilterUseCase,
                                              var quickFilterUseCase: QuickFilterUseCase,
                                              var getProductListUseCase: GetProductListUseCase,
                                              var seamlessLoginUsecase: SeamlessLoginUsecase?) : ViewModel() {


    val mProductList = MutableLiveData<Result<List<ProductsItem>>>()
    val mProductCount = MutableLiveData<String>()
    val mSubCategoryList = MutableLiveData<Result<List<SubCategoryItem>>>()
    var mDynamicFilterModel = MutableLiveData<Result<DynamicFilterModel>>()
    var mQuickFilterModel = MutableLiveData<Result<List<Filter>>>()
    val mSeamlessLogin: MutableLiveData<Result<String>> by lazy { MutableLiveData<Result<String>>() }

    fun fetchProductListing(params: RequestParams) {
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
            }
        })
    }

    fun fetchSubCategoriesList(params: RequestParams) {
        subCategoryUseCaseV3.execute(params, object : Subscriber<Data?>() {
            override fun onNext(data: Data?) {
                data?.let {
                    val subCategoryList = it.child
                    subCategoryList?.let {
                        if (subCategoryList.isNotEmpty()) {
                            val mSubCategoryArrayList = it as ArrayList<SubCategoryItem>
                            val item = SubCategoryItem()
                            item.name = "Semua"
                            item.is_default = true
                            mSubCategoryArrayList.add(0, item)
                            mSubCategoryList.value = Success(mSubCategoryArrayList)
                        } else {
                            mSubCategoryList.value = Fail(Throwable("no data"))
                        }
                    }
                }

            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                mSubCategoryList.value = Fail(e)
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

    fun getDynamicFilterData(): MutableLiveData<Result<DynamicFilterModel>> {
        return mDynamicFilterModel
    }

    fun openBrowserSeamlessly(bannedData: Data) {
        seamlessLoginUsecase?.generateSeamlessUrl(Uri.parse(bannedData.appRedirection).toString(), seamlessLoginSubscriber)
    }

    val seamlessLoginSubscriber: SeamlessLoginSubscriber? = object : SeamlessLoginSubscriber {
        override fun onUrlGenerated(url: String) {
            mSeamlessLogin.value = Success(url)
        }

        override fun onError(msg: String) {
            mSeamlessLogin.value = Fail(Throwable(msg))
        }

    }

    override fun onCleared() {
        super.onCleared()
        subCategoryUseCaseV3.unsubscribe()
        dynamicFilterUseCase.unsubscribe()
        quickFilterUseCase.unsubscribe()
        getProductListUseCase.unsubscribe()
        seamlessLoginUsecase = null
    }
}