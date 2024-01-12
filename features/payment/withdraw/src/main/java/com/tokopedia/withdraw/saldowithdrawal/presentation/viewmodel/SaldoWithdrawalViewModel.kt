package com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import com.tokopedia.withdraw.saldowithdrawal.domain.model.BankAccount
import com.tokopedia.withdraw.saldowithdrawal.domain.model.BannerData
import com.tokopedia.withdraw.saldowithdrawal.domain.model.GqlBankListResponse
import com.tokopedia.withdraw.saldowithdrawal.domain.model.ValidatePopUpWithdrawal
import com.tokopedia.withdraw.saldowithdrawal.domain.usecase.GQLValidateWithdrawalUseCase
import com.tokopedia.withdraw.saldowithdrawal.domain.usecase.GetBankListUseCase
import com.tokopedia.withdraw.saldowithdrawal.domain.usecase.GetTopadsAutoTopupWithdrawRecomUseCase
import com.tokopedia.withdraw.saldowithdrawal.domain.usecase.GetWDBannerUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import javax.inject.Inject

class SaldoWithdrawalViewModel @Inject constructor(
    private val bankListUseCase: GetBankListUseCase,
    private val bannerDataUseCase: GetWDBannerUseCase,
    private val validatePopUpUseCase: GQLValidateWithdrawalUseCase,
    private val topadsAutoTopupWithdrawRecomUseCase: GetTopadsAutoTopupWithdrawRecomUseCase,
    dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val bannerListLiveData = MutableLiveData<Result<ArrayList<BannerData>>>()

    val bankListResponseMutableData = MutableLiveData<Result<GqlBankListResponse>>()

    val validatePopUpWithdrawalMutableData = SingleLiveEvent<Result<ValidatePopUpWithdrawal>>()

    val shouldOpenTopadsAutoTopupWithdrawRecomBottomSheet = MutableLiveData<Pair<Boolean, Long>>()

    fun getValidatePopUpData(bankAccount: BankAccount, shopID: String, withdrawalAmount: Long, isSeller: Boolean) {
        launchCatchError(block = {
            val popUp = async { validatePopUpUseCase.getValidatePopUpData(bankAccount) }

            if (isSeller) {
                when (val topadsAutoTopupRecom = topadsAutoTopupWithdrawRecomUseCase(shopID)) {
                    is Success -> {
                        val recomAmount =
                            topadsAutoTopupRecom.data.topAdsAutoTopupWithdrawalRecom.data.recommendationValue
                        val isWDAmountGreaterThanRecom = withdrawalAmount > recomAmount
                        val isAutoTopadsActive =
                            topadsAutoTopupRecom.data.topAdsAutoTopupWithdrawalRecom.data.autoTopUpStatus > Int.ZERO
                        shouldOpenTopadsAutoTopupWithdrawRecomBottomSheet.postValue(
                            Pair(isWDAmountGreaterThanRecom && isAutoTopadsActive, recomAmount.toLong())
                        )
                    }

                    is Fail -> {
                        shouldOpenTopadsAutoTopupWithdrawRecomBottomSheet.postValue(
                            Pair(
                                false,
                                Long.ZERO
                            )
                        )
                    }
                }
            } else {
                shouldOpenTopadsAutoTopupWithdrawRecomBottomSheet.postValue(
                    Pair(
                        false,
                        Long.ZERO
                    )
                )
            }

            when (val popUpData = popUp.await()) {
                is Success -> {
                    validatePopUpWithdrawalMutableData
                        .postValue(Success(popUpData.data.validatePopUpWithdrawal))
                }
                is Fail -> {
                    validatePopUpWithdrawalMutableData.postValue(popUpData)
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
            when (val result = bankListUseCase.getBankList(false)) {
                is Success -> {
                    bankListResponseMutableData.postValue(Success(result.data.bankAccount))
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
