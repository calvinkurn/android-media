package com.tokopedia.withdraw.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.withdraw.domain.coroutine.usecase.GQLValidateWithdrawalUseCase
import com.tokopedia.withdraw.domain.model.BankAccount
import com.tokopedia.withdraw.domain.model.validatePopUp.ValidatePopUpWithdrawal
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ValidatePopUpViewModel @Inject constructor(
        private val gqlValidateWithdrawalUseCase: GQLValidateWithdrawalUseCase,
        dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {


    val validatePopUpWithdrawalMutableData = MutableLiveData<Result<ValidatePopUpWithdrawal>>()

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