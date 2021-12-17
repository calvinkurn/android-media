package com.tokopedia.saldodetails.transactionDetailPages.withdrawal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.saldodetails.commom.di.module.DispatcherModule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class WithdrawalDetailViewModel @Inject constructor(
    private val getWithdrawalInfoUseCase: GetWithdrawalInfoUseCase,
    @Named(DispatcherModule.MAIN) val dispatcher: CoroutineDispatcher,
): BaseViewModel(dispatcher) {

    private val _withdrawalInfoLiveData = MutableLiveData<Result<WithdrawalInfoData>>()
    val withdrawalInfoLiveData : LiveData<Result<WithdrawalInfoData>> = _withdrawalInfoLiveData

    fun getWithdrawalInfo(withdrawalId: String) {
        getWithdrawalInfoUseCase.cancelJobs()
        getWithdrawalInfoUseCase.getWithdrawalInfo(
            ::onSuccessWithdrawalInfo,
            ::onErrorWithdrawalInfo,
            withdrawalId)
    }

    private fun onSuccessWithdrawalInfo(withdrawalInfo: WithdrawalInfoData) {
        _withdrawalInfoLiveData.postValue(Success(withdrawalInfo))
    }

    private fun onErrorWithdrawalInfo(throwable: Throwable) {
        _withdrawalInfoLiveData.postValue(Fail(throwable))
    }

    override fun onCleared() {
        super.onCleared()
        getWithdrawalInfoUseCase.cancelJobs()
    }

}