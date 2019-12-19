package com.tokopedia.discovery.catalogrevamp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.discovery.catalogrevamp.model.ProductCatalogResponse
import com.tokopedia.discovery.catalogrevamp.usecase.GetProductCatalogOneUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber
import javax.inject.Inject

class CatalogDetailPageViewModel @Inject constructor(var getProductCatalogOneUseCase: GetProductCatalogOneUseCase) : ViewModel() {

    private val productCatalogResponse = MutableLiveData<Result<ProductCatalogResponse>>()

    fun getProductCatalog(catalogId: String) {
        getProductCatalogOneUseCase.execute(getProductCatalogOneUseCase.createRequestParams(catalogId), object : Subscriber<ProductCatalogResponse>() {
            override fun onNext(t: ProductCatalogResponse?) {
                productCatalogResponse.value = Success((t as ProductCatalogResponse))
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                productCatalogResponse.value = Fail(e)
            }

        })

    }

    fun getProductCatalogResponse(): MutableLiveData<Result<ProductCatalogResponse>> {
        return productCatalogResponse
    }

}