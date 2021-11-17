package com.tokopedia.cmhomewidget.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.cmhomewidget.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetDataResponse
import com.tokopedia.cmhomewidget.domain.usecase.GetCMHomeWidgetDataUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

// todo delete cm home widget dummy things
class DummyTestCMHomeWidgetViewModel @Inject constructor(
    private val getCMHomeWidgetDataUseCase: GetCMHomeWidgetDataUseCase,
    @CoroutineMainDispatcher val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _productDetailLiveData = MutableLiveData<Result<CMHomeWidgetDataResponse>>()
    val productDetailLiveData: LiveData<Result<CMHomeWidgetDataResponse>> = _productDetailLiveData

    fun getCMHomeWidgetData() {
        getCMHomeWidgetDataUseCase.getCMHomeWidgetData(::onSuccessData, ::onFailData)
    }

    private fun onSuccessData(cmHomeWidgetResponse: CMHomeWidgetDataResponse) {
        _productDetailLiveData.value = Success(cmHomeWidgetResponse)
    }

    private fun onFailData(throwable: Throwable) {
        _productDetailLiveData.value = Fail(throwable)
    }

}