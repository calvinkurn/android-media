package com.tokopedia.discovery.categoryrevamp.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductListResponse
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductsItem
import com.tokopedia.discovery.categoryrevamp.data.subCategoryModel.SubCategoryItem
import com.tokopedia.discovery.categoryrevamp.domain.usecase.CategoryProductUseCase
import com.tokopedia.discovery.categoryrevamp.domain.usecase.DynamicFilterUseCase
import com.tokopedia.discovery.categoryrevamp.domain.usecase.QuickFilterUseCase
import com.tokopedia.discovery.categoryrevamp.domain.usecase.SubCategoryUseCase
import com.tokopedia.discovery.common.data.DynamicFilterModel
import com.tokopedia.discovery.common.data.Filter
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber
import javax.inject.Inject

class ProductNavViewModel @Inject constructor(var categoryProductUseCase: CategoryProductUseCase,
                                              var subCategoryUseCase: SubCategoryUseCase,
                                              var dynamicFilterUseCase: DynamicFilterUseCase,
                                              var quickFilterUseCase: QuickFilterUseCase) : ViewModel() {


    val mProductList = MutableLiveData<Result<List<ProductsItem>>>()
    val mProductCount = MutableLiveData<String>()
    val mSubCategoryList = MutableLiveData<Result<List<SubCategoryItem>>>()
    var mDynamicFilterModel = MutableLiveData<Result<DynamicFilterModel>>()
    var mQuickFilterModel = MutableLiveData<Result<List<Filter>>>()


    fun fetchProductList(params: RequestParams) {

        categoryProductUseCase.execute(params, object : Subscriber<ProductListResponse>() {
            override fun onNext(productListResponse: ProductListResponse?) {
                productListResponse?.let { productResponse ->
                    (productResponse.searchProduct)?.let { searchProduct ->
                        searchProduct.products?.let { productList ->
                            if (productList.isNotEmpty()) {
                                mProductList.value = Success((productList) as List<ProductsItem>)
                            } else {
                                mProductList.value = Fail(Throwable("no data"))
                            }
                        }

                        mProductCount.value = searchProduct.totalData.toString()
                    }
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                mProductList.value = Fail(e)
            }
        })
    }

    fun fetchSubCategoriesList(params: RequestParams) {

        subCategoryUseCase.execute(params, object : Subscriber<ArrayList<SubCategoryItem?>?>() {
            override fun onNext(subCategoryList: ArrayList<SubCategoryItem?>?) {

                subCategoryList?.let {
                    if (subCategoryList.isNotEmpty()) {
                        mSubCategoryList.value = Success(it as List<SubCategoryItem>)
                    } else {
                        mSubCategoryList.value = Fail(Throwable("no data"))
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

}