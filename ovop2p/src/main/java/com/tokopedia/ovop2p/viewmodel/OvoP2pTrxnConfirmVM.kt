package com.tokopedia.ovop2p.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.tokopedia.graphql.data.model.GraphqlResponse

import com.tokopedia.ovop2p.model.OvoP2pTransferConfirmBase
import com.tokopedia.ovop2p.model.OvoP2pTransferRequestBase
import com.tokopedia.ovop2p.util.OvoP2pUtil
import rx.Subscriber

class OvoP2pTrxnConfirmVM(application: Application) : AndroidViewModel(application) {

    var txnConfirmMutableLiveData = MutableLiveData<OvoP2pTransferConfirmBase>()
    private var transferConfirmSubscriber: Subscriber<GraphqlResponse>? = null

    fun makeTransferConfirmCall(context: Context, transferReqMap: HashMap<String, Any>) {
        OvoP2pUtil.executeOvoP2pTransferConfirm(context, getTransferConfirmSubscriber(), transferReqMap)
    }

    override fun onCleared() {
        super.onCleared()
        if (transferConfirmSubscriber != null) {
            transferConfirmSubscriber!!.unsubscribe()
        }
    }

    fun getTransferConfirmSubscriber(): Subscriber<GraphqlResponse> {
        transferConfirmSubscriber = object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
            }

            override fun onNext(graphqlResponse: GraphqlResponse) {
                val ovoP2pTransferConfirmBase = graphqlResponse.getData<OvoP2pTransferConfirmBase>(OvoP2pTransferConfirmBase::class.java)
                if (ovoP2pTransferConfirmBase?.ovoP2pTransferConfirm != null) {
                    txnConfirmMutableLiveData!!.value = ovoP2pTransferConfirmBase
                }
            }
        }
        return transferConfirmSubscriber as Subscriber<GraphqlResponse>
    }
}
