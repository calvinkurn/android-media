package com.tokopedia.ovop2p.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.ovop2p.Constants

import com.tokopedia.ovop2p.model.WalletDataBase
import com.tokopedia.ovop2p.util.OvoP2pUtil
import com.tokopedia.ovop2p.view.viewStates.WalletBalanceState
import com.tokopedia.ovop2p.view.viewStates.WalletData
import com.tokopedia.ovop2p.view.viewStates.WalletError
import rx.Subscriber

class GetWalletBalanceViewModel(application: Application) : AndroidViewModel(application) {
    var walletLiveData: MutableLiveData<WalletBalanceState> = MutableLiveData()
    var walletBalanceSubscriber: Subscriber<GraphqlResponse>? = null
    fun fetchWalletDetails(context: Context) {
        OvoP2pUtil.executeOvoGetWalletData(context, getWalletDataSubscriber())
    }

    override fun onCleared() {
        super.onCleared()
        if (walletBalanceSubscriber != null) {
            walletBalanceSubscriber!!.unsubscribe()
        }
    }

    private fun getWalletDataSubscriber(): Subscriber<GraphqlResponse>{
        walletBalanceSubscriber =  (object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                walletLiveData?.value = WalletError(Constants.Messages.GENERAL_ERROR)
            }

            override fun onNext(graphqlResponse: GraphqlResponse) {
                val walletData = graphqlResponse.getData<WalletDataBase>(WalletDataBase::class.java)
                if (walletData != null) {
                    walletData.wallet.let { wallet ->
                        wallet?.errors?.let {
                            if(it.isNotEmpty()){
                                walletLiveData?.value = WalletError(it[0].message)
                                return
                            }
                        }
                        var cashBal = wallet?.cashBalance ?: ""
                        cashBal =  Constants.Prefixes.SALDO + cashBal
                        var sndrAmt = wallet?.rawCashBalance?.toLong() ?: 0
                        walletLiveData?.value = WalletData(cashBal, sndrAmt)
                    }
                }
                else{
                    walletLiveData?.value = WalletError(Constants.Messages.GENERAL_ERROR)
                }
            }
        })
        return walletBalanceSubscriber as Subscriber<GraphqlResponse>
    }
}
