package com.tokopedia.ovop2p.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Context

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.ovop2p.model.OvoP2pTransferRequestBase
import com.tokopedia.ovop2p.util.OvoP2pUtil

import java.util.HashMap

import rx.Subscriber

class OvoP2pTransferRequestViewModel(application: Application) : AndroidViewModel(application) {

    var ovoP2pTransferRequestBaseMutableLiveData = MutableLiveData<OvoP2pTransferRequestBase>()
    private var transferRequestSubscriber: Subscriber<GraphqlResponse>? = null

    fun makeTransferRequestCall(context: Context, transferReqMap: HashMap<String, Any>) {
        OvoP2pUtil.executeOvoP2pTransferRequest(context, getTransferRequestSubscriber(), transferReqMap)
    }

    private fun getTransferRequestSubscriber(): Subscriber<GraphqlResponse> {
        transferRequestSubscriber = object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                //stop loading
                //show error
            }

            override fun onNext(graphqlResponse: GraphqlResponse) {
                val ovoP2pTransferRequestBase = graphqlResponse.getData<OvoP2pTransferRequestBase>(OvoP2pTransferRequestBase::class.java)
                if (ovoP2pTransferRequestBase != null && ovoP2pTransferRequestBase.ovoP2pTransferRequest != null &&
                        ovoP2pTransferRequestBase.ovoP2pTransferRequest.errors == null) {
                    //execute success
                    ovoP2pTransferRequestBaseMutableLiveData!!.postValue(ovoP2pTransferRequestBase)
                } else {
                    //execute failure
                }
            }
        }
        return transferRequestSubscriber as Subscriber<GraphqlResponse>
    }

    override fun onCleared() {
        super.onCleared()
        if (transferRequestSubscriber != null) {
            transferRequestSubscriber!!.unsubscribe()
        }
    }
}
