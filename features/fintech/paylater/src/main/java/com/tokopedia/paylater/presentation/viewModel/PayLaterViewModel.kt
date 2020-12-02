package com.tokopedia.paylater.presentation.viewModel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.paylater.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.paylater.domain.model.PayLaterProductData
import com.tokopedia.paylater.domain.usecase.PayLaterDataUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class PayLaterViewModel @Inject constructor(
        private val payLaterDataUseCase: PayLaterDataUseCase,
        @CoroutineMainDispatcher dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

    fun getPayLaterData() {
        payLaterDataUseCase.cancelJobs()
        payLaterDataUseCase.getPayLaterData(
                ::onPayLaterDataSuccess,
                ::onPayLaterDataError
        )
    }

    private fun onPayLaterDataError(throwable: Throwable) {

    }

    private fun onPayLaterDataSuccess(productDataList: List<PayLaterProductData>) {

    }

    override fun onCleared() {
        payLaterDataUseCase.cancelJobs()
        super.onCleared()
    }
}