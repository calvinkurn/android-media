package com.tokopedia.paylater.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.paylater.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.paylater.domain.model.PayLaterProductData
import com.tokopedia.paylater.domain.usecase.PayLaterDataUseCase
import com.tokopedia.usecase.coroutines.Fail
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success


class PayLaterViewModel @Inject constructor(
        private val payLaterDataUseCase: PayLaterDataUseCase,
        @CoroutineMainDispatcher dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

    val payLaterActivityResultLiveData = MutableLiveData<Result<PayLaterProductData>>()

    fun getPayLaterData() {
        payLaterDataUseCase.cancelJobs()
        payLaterDataUseCase.getPayLaterData(
                ::onPayLaterDataSuccess,
                ::onPayLaterDataError
        )
    }

    private fun onPayLaterDataError(throwable: Throwable) {
        payLaterActivityResultLiveData.value = Fail(throwable)
    }

    private fun onPayLaterDataSuccess(productDataList: PayLaterProductData) {
        payLaterActivityResultLiveData.value = Success(productDataList)
    }

    override fun onCleared() {
        payLaterDataUseCase.cancelJobs()
        super.onCleared()
    }
}