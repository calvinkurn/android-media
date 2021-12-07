package com.tokopedia.cmhomewidget.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.cmhomewidget.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.cmhomewidget.di.scope.DummyTestCMHomeWidgetActivityScope
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetDataResponse
import com.tokopedia.cmhomewidget.domain.data.DeleteCMHomeWidgetDataResponse
import com.tokopedia.cmhomewidget.domain.usecase.DeleteCMHomeWidgetUseCase
import com.tokopedia.cmhomewidget.domain.usecase.GetCMHomeWidgetDataUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

// todo delete cm home widget dummy things

@DummyTestCMHomeWidgetActivityScope
class DummyTestCMHomeWidgetViewModel @Inject constructor(
    private val getCMHomeWidgetDataUseCase: GetCMHomeWidgetDataUseCase,
    private val deleteCMHomeWidgetUseCase: DeleteCMHomeWidgetUseCase,
    @CoroutineMainDispatcher val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _getCMHomeWidgetDataLiveData = MutableLiveData<Result<CMHomeWidgetDataResponse>>()
    val getCMHomeWidgetDataLiveData: LiveData<Result<CMHomeWidgetDataResponse>>
        get() = _getCMHomeWidgetDataLiveData

    private val _deleteCMHomeWidgetDataLiveData =
        MutableLiveData<Result<DeleteCMHomeWidgetDataResponse>>()
    val deleteCMHomeWidgetDataLiveData: LiveData<Result<DeleteCMHomeWidgetDataResponse>>
        get() = _deleteCMHomeWidgetDataLiveData

    fun getCMHomeWidgetData() {
        getCMHomeWidgetDataUseCase.getCMHomeWidgetData(
            ::onGetCMHomeWidgetSuccessData,
            ::onGetCMHomeWidgetFailData,
            true
        )
    }

    private fun onGetCMHomeWidgetSuccessData(cmHomeWidgetResponse: CMHomeWidgetDataResponse) {
        _getCMHomeWidgetDataLiveData.value = Success(cmHomeWidgetResponse)
    }

    private fun onGetCMHomeWidgetFailData(throwable: Throwable) {
        _getCMHomeWidgetDataLiveData.value = Fail(throwable)
    }

    fun deleteCMHomeWidgetData(productId: Long, campaignId: Long) {
        deleteCMHomeWidgetUseCase.deleteCMHomeWidgetData(
            ::onDeleteCMHomeWidgetSuccessData,
            ::onDeleteCMHomeWidgetFailData,
            productId,
            campaignId
        )
    }

    private fun onDeleteCMHomeWidgetSuccessData(deleteCMHomeWidgetDataResponse: DeleteCMHomeWidgetDataResponse) {
        _deleteCMHomeWidgetDataLiveData.value = Success(deleteCMHomeWidgetDataResponse)
    }

    private fun onDeleteCMHomeWidgetFailData(throwable: Throwable) {
        _deleteCMHomeWidgetDataLiveData.value = Fail(throwable)
    }

}