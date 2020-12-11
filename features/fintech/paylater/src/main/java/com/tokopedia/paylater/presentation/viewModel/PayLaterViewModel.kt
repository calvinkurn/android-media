package com.tokopedia.paylater.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.paylater.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.paylater.domain.model.PayLaterProductData
import com.tokopedia.paylater.domain.model.UserCreditApplicationStatus
import com.tokopedia.paylater.domain.usecase.PayLaterApplicationStatusUseCase
import com.tokopedia.paylater.domain.usecase.PayLaterDataUseCase
import com.tokopedia.usecase.coroutines.Fail
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success


class PayLaterViewModel @Inject constructor(
        private val payLaterDataUseCase: PayLaterDataUseCase,
        private val payLaterApplicationStatusUseCase: PayLaterApplicationStatusUseCase,
        @CoroutineMainDispatcher dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

    val payLaterActivityResultLiveData = MutableLiveData<Result<PayLaterProductData>>()
    val payLaterApplicationStatusResultLiveData = MutableLiveData<Result<UserCreditApplicationStatus>>()


    fun getPayLaterData() {
        payLaterDataUseCase.cancelJobs()
        payLaterDataUseCase.getPayLaterData(
                ::onPayLaterDataSuccess,
                ::onPayLaterDataError
        )
    }

    fun getPayLaterApplicationStatus() {
        payLaterApplicationStatusUseCase.cancelJobs()
        payLaterApplicationStatusUseCase.getPayLaterApplicationStaus(
                ::onPayLaterApplicationStatusSuccess,
                ::onPayLaterApplicationStatusError
        )
    }

    private fun onPayLaterApplicationStatusError(throwable: Throwable) {
        payLaterApplicationStatusResultLiveData.value = Fail(throwable)
    }

    private fun onPayLaterApplicationStatusSuccess(userCreditApplicationStatus: UserCreditApplicationStatus) {
        payLaterApplicationStatusResultLiveData.value = Success(userCreditApplicationStatus)
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