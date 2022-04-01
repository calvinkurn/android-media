package com.tokopedia.ovop2p.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.domain.model.OvoP2pTransferConfirmBase
import com.tokopedia.ovop2p.domain.usecase.OvoTrxnConfirmationUseCase
import com.tokopedia.ovop2p.view.fragment.OvoP2PForm
import com.tokopedia.ovop2p.view.viewStates.GoToThankYouPage
import com.tokopedia.ovop2p.view.viewStates.OpenPinChlngWebView
import com.tokopedia.ovop2p.view.viewStates.TransferConfErrorPage
import com.tokopedia.ovop2p.view.viewStates.TransferConfErrorSnkBar
import com.tokopedia.ovop2p.view.viewStates.TransferConfirmState
import javax.inject.Inject

class OvoP2pTrxnConfirmVM @Inject constructor(
    private val ovoTrxnConfirmationUseCase: OvoTrxnConfirmationUseCase
) : ViewModel() {

    var txnConfirmMutableLiveData = MutableLiveData<TransferConfirmState>()
    // private var transferConfirmSubscriber: Subscriber<GraphqlResponse>? = null

    fun makeTransferConfirmCall(transferReqMap: HashMap<String, Any>) {
        ovoTrxnConfirmationUseCase.getConfirmTransactionData(
            ::onSuccessConfirm,
            ::onFailConfirm,
            transferReqMap
        )
        //   OvoP2pUtil.executeOvoP2pTransferConfirm(context, getTransferConfirmSubscriber(context), transferReqMap)
    }

    private fun onSuccessConfirm(ovoP2pTransferConfirmBase: OvoP2pTransferConfirmBase) {
        ovoP2pTransferConfirmBase.ovoP2pTransferConfirm?.let { confObj ->
            confObj.errors?.let { errList ->
                if (errList.isNotEmpty()) {
                    txnConfirmMutableLiveData.value =
                        errList[0][Constants.Keys.MESSAGE]?.let { errMsg ->
                            TransferConfErrorPage(errMsg)
                        }
                } else {
                    if (!TextUtils.isEmpty(confObj.pinUrl)) {
                        txnConfirmMutableLiveData.value =
                            confObj.pinUrl?.let { OpenPinChlngWebView(it) }
                    } else {
                        txnConfirmMutableLiveData.value =
                            confObj.transferId?.let { GoToThankYouPage(it) }
                    }
                }
            } ?: kotlin.run {
                if (!TextUtils.isEmpty(confObj.pinUrl)) {
                    txnConfirmMutableLiveData.value =
                        confObj.pinUrl?.let { OpenPinChlngWebView(it) }
                } else {
                    txnConfirmMutableLiveData.value =
                        confObj.transferId?.let { GoToThankYouPage(it) }
                }
            }
        }
    }

    private fun onFailConfirm(throwable: Throwable?) {
        txnConfirmMutableLiveData.value = TransferConfErrorSnkBar(OvoP2PForm.GENERAL_ERROR)
    }

    override fun onCleared() {
        super.onCleared()
        ovoTrxnConfirmationUseCase.cancelJobs()
//        if (transferConfirmSubscriber != null) {
//            transferConfirmSubscriber!!.unsubscribe()
//        }
    }

//    fun getTransferConfirmSubscriber(context: Context): Subscriber<GraphqlResponse> {
//        transferConfirmSubscriber = object : Subscriber<GraphqlResponse>() {
//            override fun onCompleted() {
//            }
//
//            override fun onError(e: Throwable) {
//                txnConfirmMutableLiveData.value = TransferConfErrorSnkBar(context.resources.getString(
//                    R.string.general_error))
//            }
//
//            override fun onNext(graphqlResponse: GraphqlResponse) {
//                val ovoP2pTransferConfirmBase = graphqlResponse.getData<OvoP2pTransferConfirmBase>(OvoP2pTransferConfirmBase::class.java)
//                ovoP2pTransferConfirmBase?.ovoP2pTransferConfirm?.let { confObj ->
//                    confObj.errors?.let { errList ->
//                        if (errList.isNotEmpty()) {
//                            txnConfirmMutableLiveData.value = errList[0][Constants.Keys.MESSAGE]?.let { errMsg ->
//                                TransferConfErrorPage(errMsg)
//                            }
//                        } else {
//                            if (!TextUtils.isEmpty(confObj.pinUrl)) {
//                                txnConfirmMutableLiveData.value = confObj.pinUrl?.let { OpenPinChlngWebView(it) }
//                            } else {
//                                txnConfirmMutableLiveData.value = confObj.transferId?.let { GoToThankYouPage(it) }
//                            }
//                        }
//                    }
//                } ?: run {
//                    txnConfirmMutableLiveData.value = TransferConfErrorSnkBar(context.resources.getString(
//                        R.string.general_error))
//                }
//            }
//        }
//        return transferConfirmSubscriber as Subscriber<GraphqlResponse>
//    }

}
