package com.tokopedia.withdraw.auto_withdrawal.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.withdraw.auto_withdrawal.domain.model.*
import com.tokopedia.withdraw.auto_withdrawal.domain.usecase.*
import com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel.util.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AutoWDSettingsViewModel @Inject constructor(
        private val autoWDStatusUseCase: AutoWDStatusUseCase,
        private val autoWDInfoUseCase: AutoWDInfoUseCase,
        private val bankAccountListUseCase: GQLBankAccountListUseCase,
        private val autoWDTNCUseCase: AutoWDTNCUseCase,
        private val autoWDUpsertUseCase: AutoWDUpsertUseCase,
        dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val autoWDStatusDataResultLiveData = MutableLiveData<Result<AutoWDStatusData>>()
    val infoAutoWDResultLiveData = MutableLiveData<Result<GetInfoAutoWD>>()
    val bankListResultLiveData = MutableLiveData<Result<ArrayList<BankAccount>>>()
    val autoWDTNCResultLiveData = MutableLiveData<Result<String>>()
    val upsertResponseLiveData = SingleLiveEvent<Result<UpsertResponse>>()

    private var isTNCLoading = false
    private var isUpsertAutoWDInProgress = false

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

    fun getAutoWDTNC() {
        if (!isTNCLoading) {
            isTNCLoading = true
            autoWDTNCUseCase.getAutoWDTNC(::onAutoWithdrawalTNCLoaded, ::onAutoWithdrawalTNCFailed)
        }
    }

    fun upsertAutoWithdrawal(request: AutoWithdrawalUpsertRequest) {
        if (!isUpsertAutoWDInProgress) {
            isUpsertAutoWDInProgress = true
            val requestParamMap = autoWDUpsertUseCase.getRequestParams(request)
            autoWDUpsertUseCase.getAutoWDUpsert(requestParamMap,
                    { upsertResponse ->
                        if (upsertResponse.code == 200)
                            upsertResponseLiveData.value = Success(upsertResponse)
                    },
                    { error ->
                        upsertResponseLiveData.value = Fail(error)
                    })
        }
    }

    private fun onAutoWithdrawalTNCLoaded(getTNCAutoWD: GetTNCAutoWD) {
        autoWDTNCResultLiveData.value = Success(getTNCAutoWD.data.template)
        isTNCLoading = false
    }

    private fun onAutoWithdrawalTNCFailed(throwable: Throwable) {
        autoWDTNCResultLiveData.value = Fail(throwable)
        isTNCLoading = false
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
        bankAccountListUseCase.cancelJobs()
        autoWDTNCUseCase.cancelJobs()
        autoWDUpsertUseCase.cancelJobs()
        super.onCleared()
    }

}
