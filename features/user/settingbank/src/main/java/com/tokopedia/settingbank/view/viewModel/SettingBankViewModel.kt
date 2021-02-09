package com.tokopedia.settingbank.view.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.settingbank.domain.model.BankAccount
import com.tokopedia.settingbank.domain.model.KYCInfo
import com.tokopedia.settingbank.domain.model.TemplateData
import com.tokopedia.settingbank.domain.usecase.DeleteBankAccountUseCase
import com.tokopedia.settingbank.domain.usecase.GetUserBankAccountUseCase
import com.tokopedia.settingbank.domain.usecase.KyCInfoUseCase
import com.tokopedia.settingbank.domain.usecase.TermsAndConditionUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SettingBankViewModel @Inject constructor(
        private val termsAndConditionUseCase: dagger.Lazy<TermsAndConditionUseCase>,
        private val getUserBankAccountUseCase: dagger.Lazy<GetUserBankAccountUseCase>,
        private val deleteBankAccountUseCase: dagger.Lazy<DeleteBankAccountUseCase>,
        private val kyCInfoUseCase: KyCInfoUseCase,
        dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {


    val termsAndConditionLiveData = MutableLiveData<Result<TemplateData>>()
    val tncNotesLiveData = MutableLiveData<Result<TemplateData>>()

    val kycInfoLiveData = MutableLiveData<Result<KYCInfo>>()
    val deleteBankAccountLiveData = MutableLiveData<Result<String>>()

    val bankAccountListLiveData = MutableLiveData<Result<List<BankAccount>>>()
    val addBankAccountStateLiveData = MutableLiveData<Boolean>()

    fun loadUserAddedBankList() {
        getUserBankAccountUseCase.get().getUserBankAccountList({
            bankAccountListLiveData.postValue(Success(it))
        }, {
            addBankAccountStateLiveData.postValue(it)
        }, {
            bankAccountListLiveData.postValue(Fail(it))
        })
    }

    fun deleteBankAccount(bankAccount: BankAccount) {
        deleteBankAccountUseCase.get().deleteBankAccount(bankAccount, {
            deleteBankAccountLiveData.postValue(Success(it))
        }, {
            deleteBankAccountLiveData.postValue(Fail(it))
        })
    }

    fun loadTermsAndCondition() {
        termsAndConditionUseCase.get().getTermsAndCondition({
            termsAndConditionLiveData.postValue(Success(it))
        }, {
            termsAndConditionLiveData.postValue(Fail(it))
        })
    }

    fun loadTermsAndConditionNotes() {
        termsAndConditionUseCase.get().getNotes({
            tncNotesLiveData.postValue(Success(it))
        }, {
            tncNotesLiveData.postValue(Fail(it))
        })
    }

    fun getKYCInfo() {
        kyCInfoUseCase.getKYCCheckInfo({
            kycInfoLiveData.postValue(Success(it))
        }, {
            kycInfoLiveData.postValue(Fail(it))
        })
    }

    override fun onCleared() {
        termsAndConditionUseCase.get().cancelJobs()
        getUserBankAccountUseCase.get().cancelJobs()
        deleteBankAccountUseCase.get().cancelJobs()
        kyCInfoUseCase.cancelJobs()
        super.onCleared()
    }


}