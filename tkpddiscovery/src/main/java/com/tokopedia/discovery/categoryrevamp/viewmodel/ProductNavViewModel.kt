package com.tokopedia.discovery.categoryrevamp.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductListResponse
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductsItem
import com.tokopedia.discovery.categoryrevamp.data.subCategoryModel.SubCategoryItem
import com.tokopedia.discovery.categoryrevamp.domain.usecase.CategoryProductUseCase
import com.tokopedia.discovery.categoryrevamp.domain.usecase.SubCategoryUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber
import javax.inject.Inject

class ProductNavViewModel @Inject constructor(var categoryProductUseCase: CategoryProductUseCase, var subCategoryUseCase: SubCategoryUseCase) : ViewModel() {

    val mProductList = MutableLiveData<Result<List<ProductsItem>>>()
    val mSubCategoryList = MutableLiveData<Result<List<SubCategoryItem>>>()

    fun fetchProductList(params: RequestParams) {

        categoryProductUseCase.execute(params, object : Subscriber<ProductListResponse>() {
            override fun onNext(productListResponse: ProductListResponse?) {
                productListResponse?.let {
                    (it.searchProduct)?.let { searchProduct ->
                        mProductList.value = Success((searchProduct.products) as List<ProductsItem>)
                    }
                }
            }

            override fun onCompleted() {
                Log.d("ProductNavViewModel", "onCompleted")
            }

            override fun onError(e: Throwable) {
                mProductList.value = Fail(e)
            }
        })
    }

    fun fetchSubCategoriesList(params: RequestParams) {

        subCategoryUseCase.execute(params, object : Subscriber<List<SubCategoryItem?>?>() {
            override fun onNext(subCategoryList: List<SubCategoryItem?>?) {
                mSubCategoryList.value = Success(subCategoryList as List<SubCategoryItem>)
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
            }
        })
    }

}