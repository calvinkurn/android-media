package com.tokopedia.saldodetails.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.saldodetails.domain.GetWithdrawalInfoUseCase
import com.tokopedia.saldodetails.response.model.saldo_detail_info.WithdrawalInfoData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class WithdrawalDetailViewModel @Inject constructor(
    private  val getWithdrawalInfoUseCase: GetWithdrawalInfoUseCase
): BaseViewModel(Dispatchers.Main) {

    private val _withdrawalInfoLiveData = MutableLiveData<Result<WithdrawalInfoData>>()
    val withdrawalInfoLiveData : LiveData<Result<WithdrawalInfoData>> = _withdrawalInfoLiveData

    fun getWithdrawalInfo(withdrawalId: String) {
        getWithdrawalInfoUseCase.cancelJobs()
        getWithdrawalInfoUseCase.getWithdrawalInfo(withdrawalId,
            ::onSuccessWithdrawalInfo,
            ::onErrorWithdrawalInfo)
    }

    private fun onSuccessWithdrawalInfo(withdrawalInfo: WithdrawalInfoData) {
        _withdrawalInfoLiveData.postValue(Success(withdrawalInfo))
    }

    private fun onErrorWithdrawalInfo(throwable: Throwable) {
        _withdrawalInfoLiveData.postValue(Fail(throwable))

    }

    override fun onCleared() {
        getWithdrawalInfoUseCase.cancelJobs()
        super.onCleared()
    }

}