package com.tokopedia.thankyou_native.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.thankyou_native.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.thankyou_native.domain.usecase.CheckWhiteListStatusUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class CheckWhiteListViewModel @Inject constructor(
        private val checkWhiteListStatusUseCase: CheckWhiteListStatusUseCase,
        @CoroutineMainDispatcher dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val whiteListResultLiveData = MutableLiveData<Result<Boolean>>()

    fun registerForSingleAuth() {
        checkWhiteListStatusUseCase.cancelJobs()
        checkWhiteListStatusUseCase.getThankPageData(
                ::onCheckWhiteListSuccess,
                ::onCheckWhiteListError
        )
    }

    private fun onCheckWhiteListSuccess(status: Boolean) {
        whiteListResultLiveData.value = Success(status)
    }

    private fun onCheckWhiteListError(throwable: Throwable) {
        whiteListResultLiveData.value = Fail(throwable)
    }

    override fun onCleared() {
        checkWhiteListStatusUseCase.cancelJobs()
        super.onCleared()
    }

}