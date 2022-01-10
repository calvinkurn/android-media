package com.tokopedia.pdp.fintech.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.pdp.fintech.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.pdp.fintech.domain.datamodel.ProductDetailClass
import com.tokopedia.pdp.fintech.domain.usecase.ProductDetailUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class FintechWidgetViewModel @Inject constructor
    (@CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
     val productDetailUseCase:ProductDetailUseCase):
    BaseViewModel(dispatcher) {

    private val _productDetailLiveData = MutableLiveData<Result<ProductDetailClass>>()
    val productDetailLiveData: LiveData<Result<ProductDetailClass>> =
        _productDetailLiveData


    fun getProductDetail(productId:String)
        {
            productDetailUseCase.getProductDetail(::onSuccessProductInfo,::onFailProductInfo,productId)
        }

    private fun onFailProductInfo(throwable: Throwable) {
        _productDetailLiveData.value = Fail(throwable)
    }

    private fun onSuccessProductInfo(productDetailClass: ProductDetailClass) {
        _productDetailLiveData.value = Success(productDetailClass)
    }

}