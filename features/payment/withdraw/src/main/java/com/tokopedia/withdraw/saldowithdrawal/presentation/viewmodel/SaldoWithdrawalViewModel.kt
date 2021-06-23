package com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import com.tokopedia.withdraw.saldowithdrawal.domain.model.BankAccount
import com.tokopedia.withdraw.saldowithdrawal.domain.model.BannerData
import com.tokopedia.withdraw.saldowithdrawal.domain.model.ValidatePopUpWithdrawal
import com.tokopedia.withdraw.saldowithdrawal.domain.usecase.GQLValidateWithdrawalUseCase
import com.tokopedia.withdraw.saldowithdrawal.domain.usecase.GetBankListUseCase
import com.tokopedia.withdraw.saldowithdrawal.domain.usecase.GetWDBannerUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SaldoWithdrawalViewModel @Inject constructor(
        private val bankListUseCase: GetBankListUseCase,
        private val bannerDataUseCase: GetWDBannerUseCase,
        private val validatePopUpUseCase: GQLValidateWithdrawalUseCase,
        dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val bannerListLiveData = MutableLiveData<Result<ArrayList<BannerData>>>()

    val bankListResponseMutableData = MutableLiveData<Result<ArrayList<BankAccount>>>()

    val validatePopUpWithdrawalMutableData = SingleLiveEvent<Result<ValidatePopUpWithdrawal>>()

    fun getValidatePopUpData(bankAccount: BankAccount) {
        launchCatchError(block = {
            when (val result = validatePopUpUseCase.getValidatePopUpData(bankAccount)) {
                is Success -> {
                    validatePopUpWithdrawalMutableData
                            .postValue(Success(result.data.validatePopUpWithdrawal))
                }
                is Fail -> {
                    validatePopUpWithdrawalMutableData.postValue(result)
                }
            }
        }, onError = {
            validatePopUpWithdrawalMutableData.postValue(Fail(it))
        })
    }

    fun getRekeningBannerDataList() {
        launchCatchError(block = {
            when (val result = bannerDataUseCase.getRekeningProgramBanner()) {
                is Success -> {
                    bannerListLiveData.postValue(Success(result.data.richieGetWDBanner.data))
                }
                is Fail -> {
                    bannerListLiveData.postValue(result)
                }
            }
        }, onError = {
            bannerListLiveData.postValue(Fail(it))
        })
    }

    fun getBankList() {
        launchCatchError(block = {
            when (val result = bankListUseCase.getBankList()) {
                is Success -> {
                    bankListResponseMutableData.postValue(Success(result.data.bankAccount.bankAccountList))
                }
                is Fail -> {
                    bankListResponseMutableData.postValue(result)
                }
            }
        }, onError = {
            bankListResponseMutableData.postValue(Fail(it))
        })
    }

}