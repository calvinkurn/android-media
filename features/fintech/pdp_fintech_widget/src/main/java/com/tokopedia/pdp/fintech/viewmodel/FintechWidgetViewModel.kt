package com.tokopedia.pdp.fintech.viewmodel


import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.pdp.fintech.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.pdp.fintech.domain.datamodel.WidgetDetail
import com.tokopedia.pdp.fintech.domain.usecase.FintechWidgetUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class FintechWidgetViewModel @Inject constructor
    (
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
    private val fintchWidgetUseCase: FintechWidgetUseCase
) :
    BaseViewModel(dispatcher) {


    private val _widgetDetailLiveData = SingleLiveEvent<Result<WidgetDetail>>()
    val widgetDetailLiveData: SingleLiveEvent<Result<WidgetDetail>> =
        _widgetDetailLiveData


    fun getWidgetData(
        productCategory: String, listOfAmount: HashMap<String, String>,
        listOfUrls: HashMap<String, String>
    ) {
        fintchWidgetUseCase.getWidgetData(
            ::onSuccessWidgetData,
            ::onFailWidgetData, productCategory, listOfAmount, listOfUrls
        )
    }

    private fun onSuccessWidgetData(widgetDetail: WidgetDetail) {
        _widgetDetailLiveData.postValue(Success(widgetDetail))
    }

    private fun onFailWidgetData(throwable: Throwable) {
        _widgetDetailLiveData.postValue(Fail(throwable))
    }

}