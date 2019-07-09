package com.tokopedia.ovop2p.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.tokopedia.graphql.data.model.GraphqlResponse

import com.tokopedia.ovop2p.model.OvoP2pTransferThankyouBase
import com.tokopedia.ovop2p.util.OvoP2pUtil
import rx.Subscriber

class OvoP2pTxnThankYouOvoUsrVM(application: Application) : AndroidViewModel(application) {

    var ovoP2pTransferThankyouBaseMutableLiveData = MutableLiveData<OvoP2pTransferThankyouBase>()
    private var transferThankyouSubscriber: Subscriber<GraphqlResponse>? = null

    override fun onCleared() {
        super.onCleared()
        if (transferThankyouSubscriber != null) {
            transferThankyouSubscriber!!.unsubscribe()
        }
    }

    fun makeThankyouDataCall(context: Context, dataMap: HashMap<String, Any>){
        OvoP2pUtil.executeOvoP2pTransferThankyou(context, getThankYouDataSubscriber(), dataMap)
    }

    fun getThankYouDataSubscriber(): Subscriber<GraphqlResponse>{
        transferThankyouSubscriber = object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                ovoP2pTransferThankyouBaseMutableLiveData?.value = null
            }

            override fun onNext(graphqlResponse: GraphqlResponse?) {
                val ovoP2pTransferConfirmBase = graphqlResponse?.getData<OvoP2pTransferThankyouBase>(OvoP2pTransferThankyouBase::class.java)
                if(ovoP2pTransferConfirmBase != null){
                    ovoP2pTransferThankyouBaseMutableLiveData?.value = ovoP2pTransferConfirmBase
                }
            }
        }
        return transferThankyouSubscriber as Subscriber<GraphqlResponse>
    }
}
