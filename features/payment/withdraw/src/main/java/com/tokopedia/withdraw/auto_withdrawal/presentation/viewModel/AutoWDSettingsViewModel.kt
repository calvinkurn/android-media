package com.tokopedia.withdraw.auto_withdrawal.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import com.tokopedia.withdraw.auto_withdrawal.domain.model.*
import com.tokopedia.withdraw.auto_withdrawal.domain.usecase.*
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
        autoWDInfoUseCase.getAutoWDInfo(
                {
                    infoAutoWDResultLiveData.value = Success(it)
                    getBankAccount()
                },
                {
                    infoAutoWDResultLiveData.value = Fail(it)
                })
    }

    fun getAutoWDStatus() {
        autoWDStatusUseCase.cancelJobs()
        autoWDStatusUseCase.getAutoWDStatus(
                {
                    autoWDStatusDataResultLiveData.value = Success(it.autoWDStatusData)
                },
                {
                    autoWDStatusDataResultLiveData.value = Fail(it)
                })
    }

    fun getBankAccount() {
        bankAccountListUseCase.cancelJobs()
        bankAccountListUseCase.getBankAccountList(
                {
                    bankListResultLiveData.value = Success(it.bankAccount.bankAccountList)
                    getAutoWDStatus()
                },
                {
                    bankListResultLiveData.value = Fail(it)
                })
    }

    fun getAutoWDTNC() {
        if (!isTNCLoading) {
            isTNCLoading = true
            autoWDTNCUseCase.getAutoWDTNC({
                autoWDTNCResultLiveData.value = Success(it.data.template)
                isTNCLoading = false
            }, {
                autoWDTNCResultLiveData.value = Fail(it)
                isTNCLoading = false
            })
        }
    }

    fun upsertAutoWithdrawal(request: AutoWithdrawalUpsertRequest) {
        if (!isUpsertAutoWDInProgress) {
            isUpsertAutoWDInProgress = true
            autoWDUpsertUseCase.getAutoWDUpsert(request,
                    { upsertResponse ->
                        if (upsertResponse.code == 200)
                            upsertResponseLiveData.value = Success(upsertResponse)
                        isUpsertAutoWDInProgress = false
                    },
                    { error ->
                        upsertResponseLiveData.value = Fail(error)
                        isUpsertAutoWDInProgress = false
                    })
        }
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
