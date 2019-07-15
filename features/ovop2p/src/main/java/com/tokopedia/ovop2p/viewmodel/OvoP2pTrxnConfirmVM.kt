package com.tokopedia.ovop2p.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.text.TextUtils
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.ovop2p.Constants

import com.tokopedia.ovop2p.model.OvoP2pTransferConfirmBase
import com.tokopedia.ovop2p.util.OvoP2pUtil
import com.tokopedia.ovop2p.view.viewStates.*
import rx.Subscriber

class OvoP2pTrxnConfirmVM(application: Application) : AndroidViewModel(application) {

    var txnConfirmMutableLiveData = MutableLiveData<TransferConfirmState>()
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
                txnConfirmMutableLiveData.value = TransferConfErrorSnkBar(Constants.Messages.GENERAL_ERROR)
            }

            override fun onNext(graphqlResponse: GraphqlResponse) {
                val ovoP2pTransferConfirmBase = graphqlResponse.getData<OvoP2pTransferConfirmBase>(OvoP2pTransferConfirmBase::class.java)
                ovoP2pTransferConfirmBase?.ovoP2pTransferConfirm?.let { confObj ->
                    confObj.errors?.let { errList ->
                        if (errList.isNotEmpty()) {
                            txnConfirmMutableLiveData.value = errList[0][Constants.Keys.MESSAGE]?.let { errMsg ->
                                TransferConfErrorPage(errMsg)
                            }
                        } else {
                            if (!TextUtils.isEmpty(confObj.pinUrl)) {
                                txnConfirmMutableLiveData.value = OpenPinChlngWebView(confObj.pinUrl)
                            } else {
                                txnConfirmMutableLiveData.value = GoToThankYouPage(confObj.transferId)
                            }
                        }
                    }
                } ?: run {
                    txnConfirmMutableLiveData.value = TransferConfErrorSnkBar(Constants.Messages.GENERAL_ERROR)
                }
            }
        }
        return transferConfirmSubscriber as Subscriber<GraphqlResponse>
    }
}
