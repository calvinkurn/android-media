package com.tokopedia.settingbank.view.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.settingbank.domain.model.AddBankRequest
import com.tokopedia.settingbank.domain.model.AddBankResponse
import com.tokopedia.settingbank.domain.model.TemplateData
import com.tokopedia.settingbank.domain.usecase.AddBankAccountUseCase
import com.tokopedia.settingbank.domain.usecase.CheckBankAccountUseCase
import com.tokopedia.settingbank.domain.usecase.TermsAndConditionUseCase
import com.tokopedia.settingbank.domain.usecase.ValidateAccountNameUseCase
import com.tokopedia.settingbank.view.viewState.AccountCheckState
import com.tokopedia.settingbank.view.viewState.AccountNameValidationResult
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddAccountViewModel @Inject constructor(
        private val addBankAccountUseCase: AddBankAccountUseCase,
        private val termsAndConditionUseCase: TermsAndConditionUseCase,
        private val checkBankAccountUseCase: CheckBankAccountUseCase,
        private val validateAccountNameUseCase: ValidateAccountNameUseCase,
        dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val addBankAccountLiveData = MutableLiveData<Result<AddBankResponse>>()

    val termsAndConditionLiveData = MutableLiveData<Result<TemplateData>>()

    val accountCheckState = MutableLiveData<AccountCheckState>()

    val accountNameValidationResult = MutableLiveData<AccountNameValidationResult>()

    fun addBank(addBankRequest: AddBankRequest) {
        addBankAccountUseCase.addBankAccount(addBankRequest, {
            addBankAccountLiveData.postValue(Success(it))
        }, {
            addBankAccountLiveData.postValue(Fail(it))
        })
    }

    fun validateManualAccountName(accountHolderName : String){
        validateAccountNameUseCase.validateAccountHolderNameLength(accountHolderName
        ) {
            accountNameValidationResult.postValue(it)
        }
    }

    fun checkAccountNumber(bankId: Long, accountNumber: String?) {
        checkBankAccountUseCase.checkBankAccount(bankId, accountNumber) {
            accountCheckState.postValue(it)
        }
    }

    fun loadTermsAndCondition() {
        termsAndConditionUseCase.getTermsAndCondition({
            termsAndConditionLiveData.postValue(Success(it))
        }, {
            termsAndConditionLiveData.postValue(Fail(it))
        })
    }

    override fun onCleared() {
        termsAndConditionUseCase.cancelJobs()
        addBankAccountUseCase.cancelJobs()
        super.onCleared()
    }
}