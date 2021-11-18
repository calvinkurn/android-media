package com.tokopedia.cmhomewidget.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.cmhomewidget.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetDataResponse
import com.tokopedia.cmhomewidget.domain.data.DeleteCMHomeWidgetDataResponse
import com.tokopedia.cmhomewidget.domain.usecase.DeleteCMHomeWidgetDataUseCase
import com.tokopedia.cmhomewidget.domain.usecase.GetCMHomeWidgetDataUseCase
import com.tokopedia.kotlin.extensions.convertMonth
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

// todo delete cm home widget dummy things
class DummyTestCMHomeWidgetViewModel @Inject constructor(
    private val getCMHomeWidgetDataUseCase: GetCMHomeWidgetDataUseCase,
    private val deleteCMHomeWidgetDataUseCase: DeleteCMHomeWidgetDataUseCase,
    @CoroutineMainDispatcher val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _getCMHomeWidgetDataLiveData = MutableLiveData<Result<CMHomeWidgetDataResponse>>()
    val getCMHomeWidgetDataLiveData: LiveData<Result<CMHomeWidgetDataResponse>> =
        _getCMHomeWidgetDataLiveData

    private val _deleteCMHomeWidgetDataLiveData =
        MutableLiveData<Result<DeleteCMHomeWidgetDataResponse>>()
    val deleteCMHomeWidgetDataLiveData: LiveData<Result<DeleteCMHomeWidgetDataResponse>> =
        _deleteCMHomeWidgetDataLiveData

    fun getCMHomeWidgetData() {
        getCMHomeWidgetDataUseCase.getCMHomeWidgetData(
            ::onGetCMHomeWidgetSuccessData,
            ::onGetCMHomeWidgetFailData
        )
    }

    private fun onGetCMHomeWidgetSuccessData(cmHomeWidgetResponse: CMHomeWidgetDataResponse) {
        _getCMHomeWidgetDataLiveData.value = Success(cmHomeWidgetResponse)
    }

    private fun onGetCMHomeWidgetFailData(throwable: Throwable) {
        _getCMHomeWidgetDataLiveData.value = Fail(throwable)
    }

    fun deleteCMHomeWidgetData(productID: Long, campaignID: Long) {
        deleteCMHomeWidgetDataUseCase.deleteCMHomeWidgetData(
            ::onDeleteCMHomeWidgetSuccessData,
            ::onDeleteCMHomeWidgetFailData,
            productID,
            campaignID
        )
    }

    private fun onDeleteCMHomeWidgetSuccessData(deleteCMHomeWidgetDataResponse: DeleteCMHomeWidgetDataResponse) {
        _deleteCMHomeWidgetDataLiveData.value = Success(deleteCMHomeWidgetDataResponse)
    }

    private fun onDeleteCMHomeWidgetFailData(throwable: Throwable) {
        _deleteCMHomeWidgetDataLiveData.value = Fail(throwable)
    }

}