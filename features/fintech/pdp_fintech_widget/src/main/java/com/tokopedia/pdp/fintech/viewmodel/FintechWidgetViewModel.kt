package com.tokopedia.pdp.fintech.viewmodel


import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.pdp.fintech.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.pdp.fintech.domain.datamodel.WidgetDetail
import com.tokopedia.pdp.fintech.domain.datamodel.WidgetDetailV3
import com.tokopedia.pdp.fintech.domain.usecase.FintechWidgetUseCase
import com.tokopedia.pdp.fintech.domain.usecase.FintechWidgetV3UseCase
import com.tokopedia.pdp.fintech.view.FintechPriceURLDataModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class FintechWidgetViewModel @Inject constructor
    (
     val fintchWidgetUseCase: FintechWidgetUseCase,
     val fintechWidgetV3UseCase: FintechWidgetV3UseCase,
     @CoroutineMainDispatcher dispatcher: CoroutineDispatcher
) :
    BaseViewModel(dispatcher) {


    private val _widgetDetailLiveData = SingleLiveEvent<Result<WidgetDetail>>()
    val widgetDetailLiveData: SingleLiveEvent<Result<WidgetDetail>> =
        _widgetDetailLiveData

    private val _widgetDetailV3LiveData = SingleLiveEvent<Result<WidgetDetailV3>>()
    val widgetDetailV3LiveData: SingleLiveEvent<Result<WidgetDetailV3>> =
        _widgetDetailV3LiveData


    fun getWidgetData(
        productCategory: String,
        listOfAmountandUrls: HashMap<String, FintechPriceURLDataModel>,
        shopId: String,
        parentId: String,
    ) {
        fintchWidgetUseCase.getWidgetData(
            ::onSuccessWidgetData,
            ::onFailWidgetData,
            productCategory,
            listOfAmountandUrls,
            shopId,
            parentId
        )
    }

    fun getWidgetV3Data(
        productCategory: String,
        listOfAmountAndUrls: HashMap<String, FintechPriceURLDataModel>,
        shopId: String,
        parentId: String,
    ) {
        fintechWidgetV3UseCase.getWidgetV3Data(
            shopId,
            parentId,
            productCategory,
            listOfAmountAndUrls,
            ::onSuccessWidgetDataV3,
            ::onFailWidgetDataV3
        )
    }

    private fun onSuccessWidgetData(widgetDetail: WidgetDetail) {
        _widgetDetailLiveData.postValue(Success(widgetDetail))
    }

    private fun onFailWidgetData(throwable: Throwable) {
        _widgetDetailLiveData.postValue(Fail(throwable))
    }

    private fun onSuccessWidgetDataV3(widgetDetailV3: WidgetDetailV3) {
        _widgetDetailV3LiveData.postValue(Success(widgetDetailV3))
    }

    private fun onFailWidgetDataV3(throwable: Throwable) {
        _widgetDetailV3LiveData.postValue(Fail(throwable))
    }
}
