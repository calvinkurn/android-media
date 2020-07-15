package com.tokopedia.withdraw.auto_withdrawal.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.withdraw.auto_withdrawal.domain.model.AutoWDStatusData
import com.tokopedia.withdraw.auto_withdrawal.domain.usecase.AutoWDStatusUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AutoWDSettingsViewModel @Inject constructor(
        private val autoWDStatusUseCase: AutoWDStatusUseCase,
        dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val thanksPageDataResultLiveData = MutableLiveData<Result<AutoWDStatusData>>()

    fun getAutoWDStatus() {
        autoWDStatusUseCase.cancelJobs()

        autoWDStatusUseCase.getAutoWDStatus(
                ::onAutoWDStatusSuccess,
                ::onAutoWDStatusError
        )
    }

    fun getAutoWDInfo() {
        autoWDStatusUseCase.cancelJobs()

        autoWDStatusUseCase.getAutoWDStatus(
                ::onAutoWDStatusSuccess,
                ::onAutoWDStatusError
        )
    }

    fun getAutoWDTNC() {
        autoWDStatusUseCase.cancelJobs()

        autoWDStatusUseCase.getAutoWDStatus(
                ::onAutoWDStatusSuccess,
                ::onAutoWDStatusError
        )
    }

    private fun onAutoWDStatusSuccess(thanksPageData: AutoWDStatusData) {
        thanksPageDataResultLiveData.value = Success(thanksPageData)
    }

    private fun onAutoWDStatusError(throwable: Throwable) {
        thanksPageDataResultLiveData.value = Fail(throwable)
    }

    override fun onCleared() {
        autoWDStatusUseCase.cancelJobs()
        super.onCleared()
    }


}