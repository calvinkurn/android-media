package com.tokopedia.pdp.fintech.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.pdp.fintech.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.pdp.fintech.domain.datamodel.ProductDetailClass
import com.tokopedia.pdp.fintech.domain.datamodel.WidgetDetail
import com.tokopedia.pdp.fintech.domain.usecase.FintechWidgetUseCase
import com.tokopedia.pdp.fintech.domain.usecase.ProductDetailUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class FintechWidgetViewModel @Inject constructor
    (@CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
     private val productDetailUseCase:ProductDetailUseCase,
     private val fintchWidgetUseCase: FintechWidgetUseCase
     ):
    BaseViewModel(dispatcher) {

    private val _productDetailLiveData = MutableLiveData<Result<ProductDetailClass>>()
    val productDetailLiveData: LiveData<Result<ProductDetailClass>> =
        _productDetailLiveData

    private val _widgetDetailLiveData = MutableLiveData<Result<WidgetDetail>>()
    val widgetDetailLiveData: LiveData<Result<WidgetDetail>> =
        _widgetDetailLiveData


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


    fun getWidgetData()
    {
        _widgetDetailLiveData.value = Success(fintchWidgetUseCase.setWidget())
    }

}