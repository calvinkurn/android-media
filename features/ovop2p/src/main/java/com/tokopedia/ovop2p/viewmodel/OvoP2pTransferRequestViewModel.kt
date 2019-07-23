package com.tokopedia.ovop2p.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Context

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.R
import com.tokopedia.ovop2p.model.OvoP2pTransferRequestBase
import com.tokopedia.ovop2p.util.OvoP2pUtil
import com.tokopedia.ovop2p.view.viewStates.*

import java.util.HashMap

import rx.Subscriber

class OvoP2pTransferRequestViewModel(application: Application) : AndroidViewModel(application) {

    var transferReqBaseMutableLiveData = MutableLiveData<TransferRequestState>()
    private var transferRequestSubscriber: Subscriber<GraphqlResponse>? = null

    fun makeTransferRequestCall(context: Context, transferReqMap: HashMap<String, Any>) {
        OvoP2pUtil.executeOvoP2pTransferRequest(context, getTransferRequestSubscriber(), transferReqMap)
    }

    private fun getTransferRequestSubscriber(): Subscriber<GraphqlResponse> {
        transferRequestSubscriber = object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                transferReqBaseMutableLiveData.value = TransferReqErrorSnkBar(getApplication<Application>().resources.getString(R.string.general_error))
            }

            override fun onNext(graphqlResponse: GraphqlResponse) {
                val ovoP2pTransferRequestBase = graphqlResponse.getData<OvoP2pTransferRequestBase>(OvoP2pTransferRequestBase::class.java)
                ovoP2pTransferRequestBase?.ovoP2pTransferRequest?.let { reqObj ->
                    reqObj.errors?.let { errList ->
                        if (errList.isNotEmpty()) {
                            errList[0][Constants.Keys.MESSAGE]?.let { errMsg ->
                                if (errMsg.contentEquals(getApplication<Application>().resources.getString(R.string.non_ovo_usr))) {
                                    transferReqBaseMutableLiveData.value = TransferReqNonOvo()
                                } else {
                                    transferReqBaseMutableLiveData.value = TransferReqErrorPage(errMsg)
                                }
                            }
                        } else {
                            transferReqBaseMutableLiveData.value = reqObj.dstAccName?.let { TransferReqData(it) }
                        }
                    }
                } ?: run {
                    transferReqBaseMutableLiveData.value = TransferReqErrorSnkBar(getApplication<Application>().resources.getString(R.string.general_error))
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
