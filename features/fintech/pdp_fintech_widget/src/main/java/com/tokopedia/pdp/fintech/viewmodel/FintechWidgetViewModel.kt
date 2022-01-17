package com.tokopedia.pdp.fintech.viewmodel


import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.pdp.fintech.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.pdp.fintech.domain.datamodel.ProductDetailClass
import com.tokopedia.pdp.fintech.domain.datamodel.WidgetDetail
import com.tokopedia.pdp.fintech.domain.usecase.FintechWidgetUseCase
import com.tokopedia.pdp.fintech.domain.usecase.ProductDetailUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class FintechWidgetViewModel @Inject constructor
    (
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
    private val productDetailUseCase: ProductDetailUseCase,
    private val fintchWidgetUseCase: FintechWidgetUseCase
) :
    BaseViewModel(dispatcher) {

    private val _productDetailLiveData = SingleLiveEvent<Result<ProductDetailClass>>()
    val productDetailLiveData: SingleLiveEvent<Result<ProductDetailClass>> =
        _productDetailLiveData

    private val _widgetDetailLiveData = SingleLiveEvent<Result<WidgetDetail>>()
    val widgetDetailLiveData: SingleLiveEvent<Result<WidgetDetail>> =
        _widgetDetailLiveData


    fun getProductDetail(productId: String) {
        productDetailUseCase.getProductDetail(
            ::onSuccessProductInfo,
            ::onFailProductInfo,
            productId
        )
    }

    private fun onFailProductInfo(throwable: Throwable) {
        _productDetailLiveData.postValue(Fail(throwable))
    }

    private fun onSuccessProductInfo(productDetailClass: ProductDetailClass) {
        _productDetailLiveData.postValue(Success(productDetailClass))
    }


    fun getWidgetData(productCategory: String, listOfAmount: List<Double>) {
        fintchWidgetUseCase.getWidgetData(
            ::onSuccessWidgetData,
            ::onFailWidgetData, productCategory, listOfAmount
        )
    }

    private fun onSuccessWidgetData(widgetDetail: WidgetDetail) {
        _widgetDetailLiveData.postValue(Success(widgetDetail))
    }

    private fun onFailWidgetData(throwable: Throwable) {
        _widgetDetailLiveData.postValue(Fail(throwable))
    }

}