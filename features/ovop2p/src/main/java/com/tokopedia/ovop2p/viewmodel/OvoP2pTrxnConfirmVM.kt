package com.tokopedia.ovop2p.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.domain.model.OvoP2pTransferConfirmBase
import com.tokopedia.ovop2p.domain.usecase.OvoTrxnConfirmationUseCase
import com.tokopedia.ovop2p.view.fragment.OvoP2PForm
import com.tokopedia.ovop2p.view.viewStates.*
import javax.inject.Inject

class OvoP2pTrxnConfirmVM @Inject constructor(
    private val ovoTrxnConfirmationUseCase: OvoTrxnConfirmationUseCase
) : ViewModel() {

    var txnConfirmMutableLiveData = MutableLiveData<TransferConfirmState>()

    fun makeTransferConfirmCall(transferReqMap: HashMap<String, Any>) {
        ovoTrxnConfirmationUseCase.getConfirmTransactionData(
            ::onSuccessConfirm,
            ::onFailConfirm,
            transferReqMap
        )
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

    private fun onFailConfirm(throwable: Throwable) {
        txnConfirmMutableLiveData.value = TransferConfErrorSnkBar(OvoP2PForm.GENERAL_ERROR)
    }

    override fun onCleared() {
        ovoTrxnConfirmationUseCase.cancelJobs()
    }

}
