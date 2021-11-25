package com.tokopedia.ovop2p.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.domain.model.WalletDataBase
import com.tokopedia.ovop2p.domain.usecase.GetWalletBalanceUseCase
import com.tokopedia.ovop2p.view.viewStates.WalletBalanceState
import com.tokopedia.ovop2p.view.viewStates.WalletData
import com.tokopedia.ovop2p.view.viewStates.WalletError
import javax.inject.Inject

class GetWalletBalanceViewModel @Inject constructor(
    private val getWalletBalanceUseCase: GetWalletBalanceUseCase
) : ViewModel() {
    var walletLiveData: MutableLiveData<WalletBalanceState> = MutableLiveData()
    fun fetchWalletDetails() {
        getWalletBalanceUseCase.getWalletDetail(::getSuccessWalletDetail, ::onFailWalletDetail)
    }

    private fun getSuccessWalletDetail(walletDataBase: WalletDataBase) {
        walletDataBase.wallet?.let { walletObj ->
            walletObj.errors?.let { errList ->
                if (errList.isNotEmpty()) {
                    walletLiveData.value = WalletError(errList[0].message)
                } else {
                    var cashBal = walletObj.cashBalance
                    cashBal = Constants.Prefixes.SALDO + cashBal
                    val sndrAmt = walletObj.rawCashBalance.toLong()
                    walletLiveData.value = WalletData(cashBal, sndrAmt)
                }
            } ?: kotlin.run {
                var cashBal = walletObj.cashBalance
                cashBal = Constants.Prefixes.SALDO + cashBal
                var sndrAmt = walletObj.rawCashBalance.toLong()
                walletLiveData.value = WalletData(cashBal, sndrAmt)
            }
        } ?: kotlin.run {
            walletLiveData.value = WalletError("Ada yang salah. Silakan coba lagi")
        }
    }

    private fun onFailWalletDetail(throwable: Throwable) {
        walletLiveData.value = WalletError("Ada yang salah. Silakan coba lagi")
    }


    override fun onCleared() {
        super.onCleared()
        getWalletBalanceUseCase.cancelJobs()
    }

}
