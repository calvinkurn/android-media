package com.tokopedia.ovop2p.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.domain.model.OvoP2pTransferRequestBase
import com.tokopedia.ovop2p.domain.usecase.OvoP2pTransferUseCase
import com.tokopedia.ovop2p.view.fragment.OvoP2PForm.Companion.GENERAL_ERROR
import com.tokopedia.ovop2p.view.viewStates.TransferReqData
import com.tokopedia.ovop2p.view.viewStates.TransferReqErrorPage
import com.tokopedia.ovop2p.view.viewStates.TransferReqErrorSnkBar
import com.tokopedia.ovop2p.view.viewStates.TransferReqNonOvo
import com.tokopedia.ovop2p.view.viewStates.TransferRequestState
import javax.inject.Inject

class OvoP2pTransferRequestViewModel @Inject constructor(
    private val ovoP2pTransferUseCase: OvoP2pTransferUseCase
) : ViewModel() {

    var transferReqBaseMutableLiveData = MutableLiveData<TransferRequestState>()
    // private var transferRequestSubscriber: Subscriber<GraphqlResponse>? = null

    fun makeTransferRequestCall(transferReqMap: HashMap<String, Any>) {
        //  OvoP2pUtil.executeOvoP2pTransferRequest(context, getTransferRequestSubscriber(context), transferReqMap)
        ovoP2pTransferUseCase.transferOvo(::onSuccessTransfer, ::onFailTransfer, transferReqMap)
    }

    private fun onSuccessTransfer(ovoP2PTransferRequestBase: OvoP2pTransferRequestBase) {
        ovoP2PTransferRequestBase.ovoP2pTransferRequest?.let { reqObj ->
            reqObj.errors?.let { errList ->
                if (errList.isNotEmpty()) {
                    errList[0][Constants.Keys.MESSAGE]?.let { errMsg ->
                        if (errMsg.contentEquals(NON_OVO_ERROR)) {
                            transferReqBaseMutableLiveData.value = TransferReqNonOvo
                        } else {
                            transferReqBaseMutableLiveData.value = TransferReqErrorPage(errMsg)
                        }
                    }
                } else {
                    transferReqBaseMutableLiveData.value =
                        reqObj.dstAccName?.let { TransferReqData(it) }
                }
            } ?: kotlin.run {
                transferReqBaseMutableLiveData.value =
                    reqObj.dstAccName?.let { TransferReqData(it) }
            }

        }
    }

    private fun onFailTransfer(throwable: Throwable?) {
        transferReqBaseMutableLiveData.value = TransferReqErrorSnkBar(GENERAL_ERROR)
    }


    override fun onCleared() {
        super.onCleared()
        ovoP2pTransferUseCase.cancelJobs()
//        if (transferRequestSubscriber != null) {
//            transferRequestSubscriber!!.unsubscribe()
//        }
    }

    companion object {
        const val NON_OVO_ERROR = "Nomor ponsel penerima tidak terdaftar sebagai pengguna OVO."
    }

//    private fun getTransferRequestSubscriber(context: Context): Subscriber<GraphqlResponse> {
//        transferRequestSubscriber = object : Subscriber<GraphqlResponse>() {
//            override fun onCompleted() {
//
//            }
//
//            override fun onError(e: Throwable) {
//                transferReqBaseMutableLiveData.value = TransferReqErrorSnkBar(context.resources.getString(
//                    R.string.general_error))
//            }
//
//            override fun onNext(graphqlResponse: GraphqlResponse) {
//                val ovoP2pTransferRequestBase = graphqlResponse.getData<OvoP2pTransferRequestBase>(OvoP2pTransferRequestBase::class.java)
//                ovoP2pTransferRequestBase?.ovoP2pTransferRequest?.let { reqObj ->
//                    reqObj.errors?.let { errList ->
//                        if (errList.isNotEmpty()) {
//                            errList[0][Constants.Keys.MESSAGE]?.let { errMsg ->
//                                if (errMsg.contentEquals(context.resources.getString(R.string.non_ovo_usr))) {
//                                    transferReqBaseMutableLiveData.value = TransferReqNonOvo()
//                                } else {
//                                    transferReqBaseMutableLiveData.value = TransferReqErrorPage(errMsg)
//                                }
//                            }
//                        } else {
//                            transferReqBaseMutableLiveData.value = reqObj.dstAccName?.let { TransferReqData(it) }
//                        }
//                    }
//                } ?: run {
//                    transferReqBaseMutableLiveData.value = TransferReqErrorSnkBar(context.resources.getString(
//                        R.string.general_error))
//                }
//            }
//        }
//        return transferRequestSubscriber as Subscriber<GraphqlResponse>
//    }
}
