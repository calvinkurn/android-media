package com.tokopedia.categorylevels.viewmodel

import androidx.lifecycle.*
import com.tokopedia.categorylevels.domain.usecase.SubCategoryV3UseCase
import com.tokopedia.common_category.model.bannedCategory.Data
import com.tokopedia.common_category.model.productModel.ProductListResponse
import com.tokopedia.common_category.model.productModel.ProductsItem
import com.tokopedia.common_category.model.productModel.SearchProduct
import com.tokopedia.common_category.usecase.DynamicFilterUseCase
import com.tokopedia.common_category.usecase.GetProductListUseCase
import com.tokopedia.common_category.usecase.QuickFilterUseCase
import com.tokopedia.common_category.usecase.SendTopAdsUseCase
import com.tokopedia.common_category.model.bannedCategory.SubCategoryItem
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
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
                                              var sendTopAdsUseCase: SendTopAdsUseCase) : ViewModel() {


    val mProductList = MutableLiveData<Result<List<ProductsItem>>>()
    val mProductCount = MutableLiveData<SearchProduct>()
    val mSubCategoryList = MutableLiveData<Result<List<SubCategoryItem>>>()
    var mDynamicFilterModel = MutableLiveData<Result<DynamicFilterModel>>()
    var mQuickFilterModel = MutableLiveData<Result<List<Filter>>>()

    fun fetchProductListing(params: RequestParams) {
        getProductListUseCase.execute(params, object : Subscriber<ProductListResponse>() {
            override fun onNext(productListResponse: ProductListResponse?) {
                productListResponse?.let { productResponse ->
                    (productResponse.searchProduct)?.let { searchProduct ->
                        searchProduct.products.let { productList ->
                            mProductList.value = Success((productList) as List<ProductsItem>)
                        }

                        mProductCount.value = searchProduct
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

    fun sendTopAdsImpressions(url: String, productId: String, productName: String, imageUrl: String) {
        sendTopAdsUseCase.hitImpressions(url, productId, productName, imageUrl)
    }

    fun sendTopAdsClick(url: String, productId: String, productName: String, imageUrl: String) {
        sendTopAdsUseCase.hitClick(url, productId, productName, imageUrl)
    }

    override fun onCleared() {
        super.onCleared()
        subCategoryUseCaseV3.unsubscribe()
        dynamicFilterUseCase.unsubscribe()
        quickFilterUseCase.unsubscribe()
        getProductListUseCase.unsubscribe()
    }
}