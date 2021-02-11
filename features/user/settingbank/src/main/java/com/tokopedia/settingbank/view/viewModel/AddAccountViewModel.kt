package com.tokopedia.settingbank.view.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.settingbank.domain.model.AddBankRequest
import com.tokopedia.settingbank.domain.model.AddBankResponse
import com.tokopedia.settingbank.domain.model.Bank
import com.tokopedia.settingbank.domain.model.TemplateData
import com.tokopedia.settingbank.domain.usecase.*
import com.tokopedia.settingbank.view.viewState.AccountCheckState
import com.tokopedia.settingbank.view.viewState.AccountNameValidationResult
import com.tokopedia.settingbank.view.viewState.ValidateAccountNumberState
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddAccountViewModel @Inject constructor(
        private val addBankAccountUseCase: dagger.Lazy<AddBankAccountUseCase>,
        private val termsAndConditionUseCase: dagger.Lazy<TermsAndConditionUseCase>,
        private val checkBankAccountUseCase: dagger.Lazy<CheckBankAccountUseCase>,
        private val validateAccountNameUseCase: dagger.Lazy<ValidateAccountNameUseCase>,
        private val validateAccountNumberUseCase: dagger.Lazy<ValidateAccountNumberUseCase>,
        dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val addBankAccountLiveData = MutableLiveData<Result<AddBankResponse>>()
    val termsAndConditionLiveData = MutableLiveData<Result<TemplateData>>()
    val validateAccountNumberStateLiveData = MutableLiveData<ValidateAccountNumberState>()
    val accountCheckState = MutableLiveData<AccountCheckState>()
    val accountNameValidationResult = MutableLiveData<AccountNameValidationResult>()

    fun addBank(addBankRequest: AddBankRequest) {
        addBankAccountUseCase.get().addBankAccount(addBankRequest, {
            addBankAccountLiveData.postValue(Success(it))
        }, {
            addBankAccountLiveData.postValue(Fail(it))
        })
    }

    fun validateManualAccountName(accountHolderName: String) {
        validateAccountNameUseCase.get().validateAccountHolderNameLength(accountHolderName
        ) {
            accountNameValidationResult.postValue(it)
        }
    }

    fun checkAccountNumber(bankId: Long, accountNumber: String?) {
        checkBankAccountUseCase.get().checkBankAccount(bankId, accountNumber) {
            accountCheckState.postValue(it)
        }
    }

    fun loadTermsAndCondition() {
        termsAndConditionUseCase.get().getTermsAndCondition({
            termsAndConditionLiveData.postValue(Success(it))
        }, {
            termsAndConditionLiveData.postValue(Fail(it))
        })
    }

    fun validateAccountNumber(bank: Bank?, accountNumberStr: String) {
        validateAccountNumberUseCase.get().cancelJobs()
        validateAccountNumberUseCase.get().onTextChanged(bank, accountNumberStr) {
            validateAccountNumberStateLiveData.postValue(it)
        }
    }


    override fun onCleared() {
        validateAccountNumberUseCase.get().cancelJobs()
        addBankAccountUseCase.get().cancelJobs()
        termsAndConditionUseCase.get().cancelJobs()
        checkBankAccountUseCase.get().cancelJobs()
        validateAccountNameUseCase.get().cancelJobs()
        super.onCleared()
    }
}