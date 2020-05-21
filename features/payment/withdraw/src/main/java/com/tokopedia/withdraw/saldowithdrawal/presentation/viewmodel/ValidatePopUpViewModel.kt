package com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.withdraw.saldowithdrawal.domain.usecase.GQLValidateWithdrawalUseCase
import com.tokopedia.withdraw.saldowithdrawal.domain.model.BankAccount
import com.tokopedia.withdraw.saldowithdrawal.domain.model.ValidatePopUpWithdrawal
import com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel.util.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ValidatePopUpViewModel @Inject constructor(
        private val gqlValidateWithdrawalUseCase: GQLValidateWithdrawalUseCase,
        dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {


    val validatePopUpWithdrawalMutableData = SingleLiveEvent<Result<ValidatePopUpWithdrawal>>()

    fun checkForValidatePopup(bankAccount: BankAccount) {
        gqlValidateWithdrawalUseCase.getValidatePopUpData(
                bankAccount,
                ::onCheckValidatePopUpLoaded,
                ::onValidatePopUpLoadingError
        )
    }

    private fun onCheckValidatePopUpLoaded(validatePopUpWithdrawal: ValidatePopUpWithdrawal) {
        validatePopUpWithdrawalMutableData.value = Success(validatePopUpWithdrawal)
    }

    private fun onValidatePopUpLoadingError(throwable: Throwable) {
        validatePopUpWithdrawalMutableData.value = Fail(throwable)
    }

}