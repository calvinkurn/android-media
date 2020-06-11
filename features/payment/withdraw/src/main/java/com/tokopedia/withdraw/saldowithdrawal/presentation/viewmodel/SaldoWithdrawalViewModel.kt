package com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.withdraw.saldowithdrawal.domain.helper.WithdrawalDomainConstant.GQL_QUERY_GET_BANK_ACCOUNT
import com.tokopedia.withdraw.saldowithdrawal.domain.helper.WithdrawalDomainConstant.GQL_QUERY_WITHDRAWAL_BANNER
import com.tokopedia.withdraw.saldowithdrawal.domain.model.BankAccount
import com.tokopedia.withdraw.saldowithdrawal.domain.model.BannerData
import com.tokopedia.withdraw.saldowithdrawal.domain.model.GetWDBannerResponse
import com.tokopedia.withdraw.saldowithdrawal.domain.model.GqlGetBankDataResponse
import com.tokopedia.withdraw.saldowithdrawal.domain.usecase.SaldoGQLUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class SaldoWithdrawalViewModel @Inject constructor(
        @Named(GQL_QUERY_WITHDRAWAL_BANNER) val bannerQuery: String,
        @Named(GQL_QUERY_GET_BANK_ACCOUNT) val bankListQuery: String,
        private val bannerDataUseCase: SaldoGQLUseCase<GetWDBannerResponse>,
        private val bankListUseCase: SaldoGQLUseCase<GqlGetBankDataResponse>,
        dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val bannerListLiveData = MutableLiveData<Result<ArrayList<BannerData>>>()

    val bankListResponseMutableData = MutableLiveData<Result<ArrayList<BankAccount>>>()

    fun getRekeningBannerDataList() {
        launchCatchError(block = {
            bannerDataUseCase.setGraphqlQuery(bannerQuery)
            bannerDataUseCase.setTypeClass(GetWDBannerResponse::class.java)
            when (val result = bannerDataUseCase.executeUseCase()) {
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
            bankListUseCase.setGraphqlQuery(bankListQuery)
            bankListUseCase.setTypeClass(GqlGetBankDataResponse::class.java)
            when (val result = bankListUseCase.executeUseCase()) {
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