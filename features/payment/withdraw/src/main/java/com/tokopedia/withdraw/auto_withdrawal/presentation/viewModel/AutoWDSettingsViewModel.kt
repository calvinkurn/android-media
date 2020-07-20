package com.tokopedia.withdraw.auto_withdrawal.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.withdraw.auto_withdrawal.domain.model.*
import com.tokopedia.withdraw.auto_withdrawal.domain.usecase.AutoWDInfoUseCase
import com.tokopedia.withdraw.auto_withdrawal.domain.usecase.AutoWDStatusUseCase
import com.tokopedia.withdraw.auto_withdrawal.domain.usecase.GQLBankAccountListUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AutoWDSettingsViewModel @Inject constructor(
        private val autoWDStatusUseCase: AutoWDStatusUseCase,
        private val autoWDInfoUseCase: AutoWDInfoUseCase,
        private val bankAccountListUseCase: GQLBankAccountListUseCase,
        dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val autoWDStatusDataResultLiveData = MutableLiveData<Result<AutoWDStatusData>>()
    val infoAutoWDResultLiveData = MutableLiveData<Result<GetInfoAutoWD>>()
    val bankListResultLiveData = MutableLiveData<Result<ArrayList<BankAccount>>>()

    fun getAutoWDInfo() {
        autoWDInfoUseCase.cancelJobs()
        autoWDInfoUseCase.getAutoWDInfo(::onAutoWithdrawalInfoLoaded, ::onAutoWithdrawalInfoFailed)
    }

    fun getAutoWDStatus() {
        autoWDStatusUseCase.cancelJobs()
        autoWDStatusUseCase.getAutoWDStatus(::onAutoWDStatusSuccess, ::onAutoWDStatusError)
    }

    fun getBankAccount() {
        bankAccountListUseCase.cancelJobs()
        bankAccountListUseCase.getBankAccountList(::onBankAccountListLoaded, ::onBankAccountListFailed)
    }


    private fun onAutoWithdrawalInfoLoaded(getInfoAutoWD: GetInfoAutoWD) {
        infoAutoWDResultLiveData.value = Success(getInfoAutoWD)
        getBankAccount()
    }

    private fun onAutoWithdrawalInfoFailed(throwable: Throwable) {
        infoAutoWDResultLiveData.value = Fail(throwable)
    }

    private fun onAutoWDStatusSuccess(autoWDStatus: AutoWDStatus) {
        autoWDStatusDataResultLiveData.value = Success(autoWDStatus.autoWDStatusData)
    }

    private fun onAutoWDStatusError(throwable: Throwable) {
        autoWDStatusDataResultLiveData.value = Fail(throwable)
    }

    private fun onBankAccountListLoaded(gqlBankListResponse: GqlGetBankDataResponse) {
        bankListResultLiveData.value = Success(gqlBankListResponse.bankAccount.bankAccountList)
        getAutoWDStatus()
    }

    private fun onBankAccountListFailed(throwable: Throwable) {
        bankListResultLiveData.value = Fail(throwable)
    }

    override fun onCleared() {
        autoWDStatusUseCase.cancelJobs()
        autoWDInfoUseCase.cancelJobs()
        super.onCleared()
    }


}

//GetInfoAutoWithdrawal -> GetAutoWithdrawalStatus -> BankAccountAPI