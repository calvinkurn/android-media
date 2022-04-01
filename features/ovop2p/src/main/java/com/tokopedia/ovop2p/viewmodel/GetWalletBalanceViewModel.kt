package com.tokopedia.ovop2p.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.domain.model.Wallet
import com.tokopedia.ovop2p.domain.model.WalletDataBase
import com.tokopedia.ovop2p.domain.usecase.GetWalletBalanceUseCase
import com.tokopedia.ovop2p.view.fragment.OvoP2PForm.Companion.GENERAL_ERROR
import com.tokopedia.ovop2p.view.viewStates.WalletBalanceState
import com.tokopedia.ovop2p.view.viewStates.WalletData
import com.tokopedia.ovop2p.view.viewStates.WalletError
import javax.inject.Inject

class GetWalletBalanceViewModel @Inject constructor(
    private val getWalletBalanceUseCase: GetWalletBalanceUseCase
) : ViewModel() {
    var walletLiveData: MutableLiveData<WalletBalanceState> = MutableLiveData()

    // var walletBalanceSubscriber: Subscriber<GraphqlResponse>? = null
    fun fetchWalletDetails() {
        // OvoP2pUtil.executeOvoGetWalletData(context, getWalletDataSubscriber(context))
        getWalletBalanceUseCase.getWalletDetail(
            ::getSuccessWalletDetail,
            ::onFailGeneralWalletDetail
        )
    }

    private fun getSuccessWalletDetail(walletDataBase: WalletDataBase) {
        walletDataBase.wallet?.let { walletObj ->
            walletObj.errors?.let { errList ->
                if (errList.isNotEmpty()) {
                    onFailErrorMessage(errList[0].message)
                } else {
                    onSuccessGetWalletDetail(walletObj)
                }
            } ?: kotlin.run {
                onSuccessGetWalletDetail(walletObj)
            }
        } ?: kotlin.run {
            onFailGeneralWalletDetail(null)
        }
    }

    private fun onSuccessGetWalletDetail(walletObj: Wallet) {
        var cashBal = walletObj.cashBalance
        cashBal = Constants.Prefixes.SALDO + cashBal
        val sndrAmt = walletObj.rawCashBalance.toLong()
        walletLiveData.value = WalletData(cashBal, sndrAmt)
    }

    private fun onFailErrorMessage(message: String) {
        walletLiveData.value = WalletError(message)
    }

    private fun onFailGeneralWalletDetail(throwable: Throwable?) {
        walletLiveData.value = WalletError(GENERAL_ERROR)
    }

//    private fun getWalletDataSubscriber(context: Context): Subscriber<GraphqlResponse> {
//        walletBalanceSubscriber = (object : Subscriber<GraphqlResponse>() {
//            override fun onCompleted() {
//
//            }
//
//            override fun onError(e: Throwable) {
//                walletLiveData.value = WalletError(context.resources.getString(R.string.general_error))
//            }
//
//            override fun onNext(graphqlResponse: GraphqlResponse) {
//                val walletData = graphqlResponse.getData<WalletDataBase>(WalletDataBase::class.java)
//                walletData?.wallet?.let { walletObj ->
//                    walletObj.errors?.let {errList ->
//                        if (errList.isNotEmpty()) {
//                            walletLiveData.value = WalletError(errList[0].message)
//                        }
//                        else{
//                            var cashBal = walletObj.cashBalance
//                            cashBal = Constants.Prefixes.SALDO + cashBal
//                            var sndrAmt = walletObj.rawCashBalance.toLong()
//                            walletLiveData.value = WalletData(cashBal, sndrAmt)
//                        }
//                    } ?: run {
//                        var cashBal = walletObj.cashBalance
//                        cashBal = Constants.Prefixes.SALDO + cashBal
//                        var sndrAmt = walletObj.rawCashBalance.toLong()
//                        walletLiveData.value = WalletData(cashBal, sndrAmt)
//                    }
//                } ?: run {
//                    walletLiveData.value = WalletError(context.resources.getString(R.string.general_error))
//                }
//            }
//        })
//        return walletBalanceSubscriber as Subscriber<GraphqlResponse>
//    }


    override fun onCleared() {
        super.onCleared()
        getWalletBalanceUseCase.cancelJobs()
//        if (walletBalanceSubscriber != null) {
//            walletBalanceSubscriber!!.unsubscribe()
//        }
    }


}
