package com.tokopedia.ovop2p.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.domain.model.OvoP2pTransferRequestBase
import com.tokopedia.ovop2p.domain.usecase.OvoP2pTransferUseCase
import com.tokopedia.ovop2p.view.viewStates.*
import java.util.*
import javax.inject.Inject

class OvoP2pTransferRequestViewModel @Inject constructor(
    private val ovoP2pTransferUseCase: OvoP2pTransferUseCase
) : ViewModel() {

    var transferReqBaseMutableLiveData = MutableLiveData<TransferRequestState>()

    fun makeTransferRequestCall(transferReqMap: HashMap<String, Any>) {
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

    private fun onFailTransfer(throwable: Throwable) {
        transferReqBaseMutableLiveData.value = TransferReqErrorSnkBar(GENERAL_ERROR)
    }


    override fun onCleared() {
        ovoP2pTransferUseCase.cancelJobs()
    }

    companion object {
        const val GENERAL_ERROR = "Ada yang salah. Silakan coba lagi"
        const val NON_OVO_ERROR = "Nomor ponsel penerima tidak terdaftar sebagai pengguna OVO."
    }
}
