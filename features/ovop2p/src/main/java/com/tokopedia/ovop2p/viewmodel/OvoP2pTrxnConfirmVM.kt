package com.tokopedia.ovop2p.viewmodel

import androidx.lifecycle.MutableLiveData
import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.ViewModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.R

import com.tokopedia.ovop2p.model.OvoP2pTransferConfirmBase
import com.tokopedia.ovop2p.util.OvoP2pUtil
import com.tokopedia.ovop2p.view.viewStates.*
import rx.Subscriber

class OvoP2pTrxnConfirmVM : ViewModel() {

    var txnConfirmMutableLiveData = MutableLiveData<TransferConfirmState>()
    private var transferConfirmSubscriber: Subscriber<GraphqlResponse>? = null

    fun makeTransferConfirmCall(context: Context, transferReqMap: HashMap<String, Any>) {
        OvoP2pUtil.executeOvoP2pTransferConfirm(context, getTransferConfirmSubscriber(context), transferReqMap)
    }

    override fun onCleared() {
        super.onCleared()
        if (transferConfirmSubscriber != null) {
            transferConfirmSubscriber!!.unsubscribe()
        }
    }

    fun getTransferConfirmSubscriber(context:Context): Subscriber<GraphqlResponse> {
        transferConfirmSubscriber = object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                txnConfirmMutableLiveData.value = TransferConfErrorSnkBar(context.resources.getString(R.string.general_error))
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
                                txnConfirmMutableLiveData.value = confObj.pinUrl?.let { OpenPinChlngWebView(it) }
                            } else {
                                txnConfirmMutableLiveData.value = confObj.transferId?.let { GoToThankYouPage(it) }
                            }
                        }
                    }
                } ?: run {
                    txnConfirmMutableLiveData.value = TransferConfErrorSnkBar(context.resources.getString(R.string.general_error))
                }
            }
        }
        return transferConfirmSubscriber as Subscriber<GraphqlResponse>
    }
}
