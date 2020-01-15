package com.tokopedia.saldodetails.viewmodels

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.saldodetails.di.DispatcherModule
import com.tokopedia.saldodetails.response.model.*
import com.tokopedia.saldodetails.usecase.*
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class SaldoDetailViewModel @Inject constructor(
        val getSaldoBalanceUseCase: GetSaldoBalanceUseCase,
        val getTickerWithdrawalMessageUseCase: GetTickerWithdrawalMessageUseCase,
        var getMerchantSaldoDetails: GetMerchantSaldoDetails,
        val getMerchantCreditDetails: GetMerchantCreditDetails,
        val getMCLLateCountUseCase: GetMCLLateCountUseCase,
        @Named(DispatcherModule.MAIN) val uiDispatcher: CoroutineDispatcher,
        @Named(DispatcherModule.IO) val workerDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {

    var isSeller: Boolean = false

    val gqlMerchantSaldoDetailLiveData: MutableLiveData<LiveDataResult<GqlMerchantSaldoDetailsResponse>> = MutableLiveData()
    val gqlMerchantCreditDetailLiveData: MutableLiveData<LiveDataResult<GqlMerchantCreditDetailsResponse>> = MutableLiveData()
    val gqlLateCountResponseLiveData: MutableLiveData<LiveDataResult<GqlMclLateCountResponse>> = MutableLiveData()
    val gqlTickerWithdrawalLiveData: MutableLiveData<LiveDataResult<GqlWithdrawalTickerResponse>> = MutableLiveData()
    val gqlUserSaldoBalanceLiveData: MutableLiveData<LiveDataResult<GqlSaldoBalanceResponse>> = MutableLiveData()

    fun getUserSaldoBalance() {
        launchCatchError(block = {
            withContext(workerDispatcher) {
                val response = getSaldoBalanceUseCase.getResponse()
                gqlUserSaldoBalanceLiveData.postValue(LiveDataResult.success(response))
            }

        }, onError = {
            it.printStackTrace()
            gqlUserSaldoBalanceLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun getTickerWithdrawalMessage() {
        launchCatchError(block = {
            withContext(workerDispatcher) {
                val response = getTickerWithdrawalMessageUseCase.getResponse()
                gqlTickerWithdrawalLiveData.postValue(LiveDataResult.success(response))
            }
        }, onError = {
            gqlTickerWithdrawalLiveData.postValue(LiveDataResult.error(it))

        })
    }

    fun getMerchantSaldoDetails() {
        launchCatchError(block = {
            withContext(workerDispatcher) {
                val response = getMerchantSaldoDetails.getResponse()
                gqlMerchantSaldoDetailLiveData.postValue(LiveDataResult.success(response))
            }
        }, onError = {
            gqlMerchantSaldoDetailLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun getMerchantCreditLineDetails() {
        launchCatchError(block = {
            val response = getMerchantCreditDetails.execute()
            gqlMerchantCreditDetailLiveData.postValue(LiveDataResult.success(response))
        }, onError = {
            gqlMerchantCreditDetailLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun getMerchantCreditLateCountValue() {
        launchCatchError(block = {
            withContext(workerDispatcher) {
                val response = getMCLLateCountUseCase.getResponse()
                gqlLateCountResponseLiveData.postValue(LiveDataResult.success(response))
            }
        }, onError = {
            gqlLateCountResponseLiveData.postValue(LiveDataResult.error(it))
        })
    }

}