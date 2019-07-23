package com.tokopedia.ovop2p.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.R

import com.tokopedia.ovop2p.model.OvoP2pTransferThankyouBase
import com.tokopedia.ovop2p.util.OvoP2pUtil
import com.tokopedia.ovop2p.view.viewStates.ThankYouErrPage
import com.tokopedia.ovop2p.view.viewStates.ThankYouErrSnkBar
import com.tokopedia.ovop2p.view.viewStates.ThankYouPageState
import com.tokopedia.ovop2p.view.viewStates.ThankYouSucs
import rx.Subscriber

class OvoP2pTxnThankYouOvoUsrVM(application: Application) : AndroidViewModel(application) {

    var transferThankyouLiveData = MutableLiveData<ThankYouPageState>()
    private var transferThankyouSubscriber: Subscriber<GraphqlResponse>? = null

    override fun onCleared() {
        super.onCleared()
        if (transferThankyouSubscriber != null) {
            transferThankyouSubscriber!!.unsubscribe()
        }
    }

    fun makeThankyouDataCall(context: Context, dataMap: HashMap<String, Any>) {
        OvoP2pUtil.executeOvoP2pTransferThankyou(context, getThankYouDataSubscriber(), dataMap)
    }

    fun getThankYouDataSubscriber(): Subscriber<GraphqlResponse> {
        transferThankyouSubscriber = object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                transferThankyouLiveData.value = ThankYouErrSnkBar(getApplication<Application>().resources.getString(R.string.general_error))
            }

            override fun onNext(graphqlResponse: GraphqlResponse?) {
                val ovoP2pTransferConfirmBase = graphqlResponse?.getData<OvoP2pTransferThankyouBase>(OvoP2pTransferThankyouBase::class.java)
                ovoP2pTransferConfirmBase?.ovoP2pTransferThankyou?.let {
                    it.errors?.let { errList ->
                        if (errList.isNotEmpty()) {
                            errList[0].let { errMap ->
                                errMap[Constants.Keys.MESSAGE]?.let { errMsg ->
                                    transferThankyouLiveData.value = ThankYouErrPage(errMsg)
                                }
                            }
                        } else {
                            transferThankyouLiveData.value = ThankYouSucs(ovoP2pTransferConfirmBase)
                        }
                    } ?: run{
                        transferThankyouLiveData.value = ThankYouSucs(ovoP2pTransferConfirmBase)
                    }
                } ?: run {
                    transferThankyouLiveData.value = ThankYouErrSnkBar(getApplication<Application>().resources.getString(R.string.general_error))
                }
            }
        }
        return transferThankyouSubscriber as Subscriber<GraphqlResponse>
    }
}
