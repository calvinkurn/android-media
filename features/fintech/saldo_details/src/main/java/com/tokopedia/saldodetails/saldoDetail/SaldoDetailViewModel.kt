package com.tokopedia.saldodetails.saldoDetail

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.commom.di.module.DispatcherModule
import com.tokopedia.saldodetails.commom.utils.ErrorMessage
import com.tokopedia.saldodetails.commom.utils.Resources
import com.tokopedia.saldodetails.commom.utils.Success
import com.tokopedia.saldodetails.saldoDetail.domain.data.*
import com.tokopedia.saldodetails.saldoDetail.domain.usecase.*
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Named

class SaldoDetailViewModel @Inject constructor(
    val getSaldoBalanceUseCase: GetSaldoBalanceUseCase,
    val getTickerWithdrawalMessageUseCase: GetTickerWithdrawalMessageUseCase,
    var getMerchantSaldoDetails: GetMerchantSaldoDetails,
    val getMCLLateCountUseCase: GetMCLLateCountUseCase,
    @Named(DispatcherModule.MAIN) val uiDispatcher: CoroutineDispatcher,
    @Named(DispatcherModule.IO) val workerDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {

    var isSeller: Boolean = false

    val gqlMerchantSaldoDetailLiveData: MutableLiveData<Resources<GqlMerchantSaldoDetailsResponse>> = MutableLiveData()
    val gqlLateCountResponseLiveData: MutableLiveData<Resources<GqlMclLateCountResponse>> = MutableLiveData()
    val gqlTickerWithdrawalLiveData: MutableLiveData<Resources<GqlWithdrawalTickerResponse>> = MutableLiveData()
    val gqlUserSaldoBalanceLiveData: MutableLiveData<Resources<GqlSaldoBalanceResponse>> = MutableLiveData()

    fun getUserSaldoBalance() {
        launchCatchError(block = {
            withContext(workerDispatcher) {
                val response = getSaldoBalanceUseCase.getResponse()
                gqlUserSaldoBalanceLiveData.postValue(Success(response))
            }

        }, onError = {
            it.printStackTrace()
            if (it is UnknownHostException ||
                    it is SocketTimeoutException) {
                gqlUserSaldoBalanceLiveData.postValue(ErrorMessage(it.toString()))
            } else {
                gqlUserSaldoBalanceLiveData.postValue(ErrorMessage(R.string.sp_empty_state_error))
            }
        })
    }

    fun getTickerWithdrawalMessage() {
        launchCatchError(block = {
            withContext(workerDispatcher) {
                val response = getTickerWithdrawalMessageUseCase.getResponse()
                gqlTickerWithdrawalLiveData.postValue(Success(response))
            }
        }, onError = {
            gqlTickerWithdrawalLiveData.postValue(ErrorMessage(it.toString()))

        })
    }

    fun getMerchantSaldoDetails() {
        launchCatchError(block = {
            withContext(workerDispatcher) {
                val response = getMerchantSaldoDetails.getResponse()
                gqlMerchantSaldoDetailLiveData.postValue(Success(response))
            }
        }, onError = {
            gqlMerchantSaldoDetailLiveData.postValue(ErrorMessage(it.toString()))
        })
    }

    fun getMerchantCreditLateCountValue() {
        launchCatchError(block = {
            withContext(workerDispatcher) {
                val response = getMCLLateCountUseCase.getResponse()
                gqlLateCountResponseLiveData.postValue(Success(response))
            }
        }, onError = {
            gqlLateCountResponseLiveData.postValue(ErrorMessage(it.toString()))
        })
    }

}
