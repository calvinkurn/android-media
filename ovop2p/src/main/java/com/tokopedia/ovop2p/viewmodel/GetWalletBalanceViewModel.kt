package com.tokopedia.ovop2p.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.ovop2p.R

import com.tokopedia.ovop2p.model.WalletDataBase
import com.tokopedia.ovop2p.util.OvoP2pUtil
import rx.Subscriber

class GetWalletBalanceViewModel(application: Application) : AndroidViewModel(application) {
    var walletLiveData: MutableLiveData<WalletDataBase>? = null

    fun fetchWalletDetails(context: Context) {
        OvoP2pUtil.executeOvoGetWalletData(context, getWalletDataSubscriber())
    }

    private fun getWalletDataSubscriber(): Subscriber<GraphqlResponse>{
        return (object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(graphqlResponse: GraphqlResponse) {
                val walletData = graphqlResponse.getData<WalletDataBase>(WalletDataBase::class.java)
                if (walletData != null && walletData!!.wallet != null) {
                    walletLiveData?.postValue(walletData)
                }

            }
        })
    }
}
