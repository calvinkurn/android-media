package com.tokopedia.catalog.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.catalog.model.datamodel.CatalogDetailDataModel
import com.tokopedia.catalog.model.raw.ProductListResponse
import com.tokopedia.catalog.usecase.detail.CatalogDetailUseCase
import com.tokopedia.catalog.usecase.listing.CatalogGetProductListUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import rx.Subscriber
import javax.inject.Inject

class CatalogDetailPageViewModel @Inject constructor(private var catalogDetailUseCase: CatalogDetailUseCase,
                                                     private val getProductListUseCase: CatalogGetProductListUseCase) : ViewModel() {

    val catalogDetailDataModel = MutableLiveData<Result<CatalogDetailDataModel>>()
    val mProductCount = MutableLiveData<String>()

    fun getProductCatalog(catalogId: String,comparedCatalogId : String,userId : String, device : String) {
        viewModelScope.launchCatchError(
                block = {
                    catalogDetailUseCase.getCatalogDetail(catalogId,comparedCatalogId,userId,device,catalogDetailDataModel)
                },
                onError = {
                    catalogDetailDataModel.value = Fail(it)
                }
        )
    }

    fun getCatalogResponseData(): MutableLiveData<Result<CatalogDetailDataModel>> {
        return catalogDetailDataModel
    }

    fun fetchProductListing(params: RequestParams) {
        getProductListUseCase.execute(params, object : Subscriber<ProductListResponse>() {
            override fun onNext(productListResponse: ProductListResponse?) {
                productListResponse?.let { productResponse ->
                    mProductCount.value = productResponse.searchProduct?.data?.totalData.toString()
                }
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable) {}
        })
    }
}