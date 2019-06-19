package com.tokopedia.ovop2p.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.tokopedia.graphql.data.model.GraphqlResponse

import com.tokopedia.ovop2p.model.OvoP2pTransferThankyouBase
import rx.Subscriber

class OvoP2pTxnSucsOvoUsrVM(application: Application) : AndroidViewModel(application) {

    var ovoP2pTransferThankyouBaseMutableLiveData: MutableLiveData<OvoP2pTransferThankyouBase>? = null
    private var transferThankyouSubscriber: Subscriber<GraphqlResponse>? = null

    override fun onCleared() {
        super.onCleared()
        if (transferThankyouSubscriber != null) {
            transferThankyouSubscriber!!.unsubscribe()
        }
    }
}
