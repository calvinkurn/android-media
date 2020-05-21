package com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.withdraw.saldowithdrawal.domain.usecase.GQLSubmitWithdrawalUseCase
import com.tokopedia.withdraw.saldowithdrawal.domain.exception.SubmitWithdrawalException
import com.tokopedia.withdraw.saldowithdrawal.domain.model.SubmitWithdrawalResponse
import com.tokopedia.withdraw.saldowithdrawal.domain.model.WithdrawalRequest
import com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel.util.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SubmitWithdrawalViewModel @Inject constructor(
        private val gqlSubmitWithdrawalUseCase: GQLSubmitWithdrawalUseCase,
        dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val submitWithdrawalResponseLiveData = SingleLiveEvent<Result<SubmitWithdrawalResponse>>()
    private var isAlreadyRequested = false

    fun submitWithdraw(withdrawalRequest: WithdrawalRequest,
                       validateToken: String) {
        if (isAlreadyRequested)
            return
        isAlreadyRequested = true
        gqlSubmitWithdrawalUseCase.submitWithdrawal(withdrawalRequest,
                validateToken, ::onRequestSubmitted, ::onRequestSubmitError)
    }

    private fun onRequestSubmitted(submitWithdrawalResponse: SubmitWithdrawalResponse) {
        if (submitWithdrawalResponse.status == WITHDRAWAL_SUCCESS) {
            submitWithdrawalResponseLiveData.postValue(Success(submitWithdrawalResponse))
        } else {
            val errorMessage = submitWithdrawalResponse.messageErrorStr ?: ""
            submitWithdrawalResponseLiveData.postValue(Fail(SubmitWithdrawalException(errorMessage)))
        }
        isAlreadyRequested = false
    }

    private fun onRequestSubmitError(throwable: Throwable) {
        submitWithdrawalResponseLiveData.postValue(Fail(throwable))
        isAlreadyRequested = false
    }

    override fun onCleared() {
        gqlSubmitWithdrawalUseCase.cancelJobs()
        super.onCleared()
    }

    companion object {
        private const val WITHDRAWAL_SUCCESS = "success"
    }

}