package com.tokopedia.ovop2p.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.R

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

    private fun getWalletDataSubscriber(): Subscriber<GraphqlResponse> {
        walletBalanceSubscriber = (object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                walletLiveData.value = WalletError(getApplication<Application>().resources.getString(R.string.general_error))
            }

            override fun onNext(graphqlResponse: GraphqlResponse) {
                val walletData = graphqlResponse.getData<WalletDataBase>(WalletDataBase::class.java)
                walletData?.wallet?.let { walletObj ->
                    walletObj.errors?.let {errList ->
                        if (errList.isNotEmpty()) {
                            walletLiveData.value = WalletError(errList[0].message)
                        }
                    } ?: run {
                        var cashBal = walletObj.cashBalance
                        cashBal = Constants.Prefixes.SALDO + cashBal
                        var sndrAmt = walletObj.rawCashBalance.toLong()
                        walletLiveData.value = WalletData(cashBal, sndrAmt)
                    }
                } ?: run {
                    walletLiveData.value = WalletError(getApplication<Application>().resources.getString(R.string.general_error))
                }
            }
        })
        return walletBalanceSubscriber as Subscriber<GraphqlResponse>
    }
}
