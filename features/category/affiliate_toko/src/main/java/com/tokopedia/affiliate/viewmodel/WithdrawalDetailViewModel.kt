package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.affiliate.usecase.AffiliateWithdrawalDetailsUseCase
import com.tokopedia.affiliate.model.response.WithdrawalInfoData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class WithdrawalDetailViewModel @Inject constructor(
        private val affiliateWithdrawalDetailsUseCase: AffiliateWithdrawalDetailsUseCase,
): BaseViewModel() {

    private val _withdrawalInfoLiveData = MutableLiveData<Result<WithdrawalInfoData>>()
    val withdrawalInfoLiveData : LiveData<Result<WithdrawalInfoData>> = _withdrawalInfoLiveData

    fun getWithdrawalInfo(withdrawalId: String) {
        launchCatchError(block = {
            affiliateWithdrawalDetailsUseCase.getWithdrawalInfo(
                    ::onSuccessWithdrawalInfo,
                    ::onErrorWithdrawalInfo,
                    withdrawalId)
        }, onError = {
           onErrorWithdrawalInfo(it)
        })
    }

    private fun onSuccessWithdrawalInfo(withdrawalInfo: WithdrawalInfoData) {
        _withdrawalInfoLiveData.postValue(Success(withdrawalInfo))
    }

    private fun onErrorWithdrawalInfo(throwable: Throwable) {
        _withdrawalInfoLiveData.postValue(Fail(throwable))
    }
}