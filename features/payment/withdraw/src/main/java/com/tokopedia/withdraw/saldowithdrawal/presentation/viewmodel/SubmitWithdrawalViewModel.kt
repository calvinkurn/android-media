package com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import com.tokopedia.withdraw.saldowithdrawal.domain.usecase.GQLSubmitWithdrawalUseCase
import com.tokopedia.withdraw.saldowithdrawal.domain.exception.SubmitWithdrawalException
import com.tokopedia.withdraw.saldowithdrawal.domain.model.SubmitWithdrawalResponse
import com.tokopedia.withdraw.saldowithdrawal.domain.model.WithdrawalRequest
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SubmitWithdrawalViewModel @Inject constructor(
        private val useCase: GQLSubmitWithdrawalUseCase,
        dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val submitWithdrawalResponseLiveData = SingleLiveEvent<Result<SubmitWithdrawalResponse>>()
    private var isAlreadyRequested = false

    fun submitWithdraw(withdrawalRequest: WithdrawalRequest,
                       validateToken: String) {
        if (isAlreadyRequested)
            return
        isAlreadyRequested = true
        launchCatchError(block = {
            val response = useCase.submitWithdrawal(withdrawalRequest,
                    validateToken)
            when (response) {
                is Success -> onRequestSubmitted(response.data.submitWithdrawalResponse)
                is Fail -> onRequestSubmitError(response.throwable)
            }
        }, onError = {
            onRequestSubmitError(it)
        })
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


    companion object {
        private const val WITHDRAWAL_SUCCESS = "success"
    }

}