package com.tokopedia.ovop2p.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.domain.model.OvoP2pTransferConfirmBase
import com.tokopedia.ovop2p.domain.model.OvoP2pTransferRequest
import com.tokopedia.ovop2p.domain.model.OvoP2pTransferRequestBase
import com.tokopedia.ovop2p.domain.usecase.GetWalletBalanceUseCase
import com.tokopedia.ovop2p.domain.usecase.OvoP2pTransferUseCase
import com.tokopedia.ovop2p.domain.usecase.OvoTrxnConfirmationUseCase
import com.tokopedia.ovop2p.view.fragment.OvoP2PForm.Companion.GENERAL_ERROR
import com.tokopedia.ovop2p.view.viewStates.GoToThankYouPage
import com.tokopedia.ovop2p.view.viewStates.OpenPinChlngWebView
import com.tokopedia.ovop2p.view.viewStates.TransferConfErrorPage
import com.tokopedia.ovop2p.view.viewStates.TransferConfErrorSnkBar
import com.tokopedia.ovop2p.view.viewStates.TransferConfirmState
import com.tokopedia.ovop2p.view.viewStates.TransferReqData
import com.tokopedia.ovop2p.view.viewStates.TransferReqErrorPage
import com.tokopedia.ovop2p.view.viewStates.TransferReqErrorSnkBar
import com.tokopedia.ovop2p.view.viewStates.TransferReqNonOvo
import com.tokopedia.ovop2p.view.viewStates.TransferRequestState
import com.tokopedia.ovop2p.view.viewStates.WalletBalanceState
import com.tokopedia.ovop2p.view.viewStates.WalletError
import javax.inject.Inject

class OvoDetailViewModel @Inject constructor(
    private val getWalletBalanceUseCase: GetWalletBalanceUseCase,
    private val ovoTrxnConfirmationUseCase: OvoTrxnConfirmationUseCase,
    private val ovoP2pTransferUseCase: OvoP2pTransferUseCase
) : ViewModel() {
    var walletLiveData: MutableLiveData<WalletBalanceState> = MutableLiveData()
    var transferReqBaseMutableLiveData = MutableLiveData<TransferRequestState>()
    var txnConfirmMutableLiveData = MutableLiveData<TransferConfirmState>()

    fun fetchWalletDetails() {
        getWalletBalanceUseCase.getWalletDetail(
            ::getSuccessWalletDetail,
            ::onFailErrorMessage
        )
    }

    private fun getSuccessWalletDetail(responseType: GetWalletBalanceUseCase.ResposeType) {
        when (responseType) {
            is GetWalletBalanceUseCase.ResposeType.FailResponse -> {
                walletLiveData.value = responseType.errorMessage?.let { error ->
                    WalletError(
                        error
                    )
                }
            }
            is GetWalletBalanceUseCase.ResposeType.SuccessResponse -> {
                walletLiveData.value = responseType.walletData
            }
        }
    }


    private fun onFailErrorMessage(responseType: GetWalletBalanceUseCase.ResposeType) {
        walletLiveData.value =
            (responseType as GetWalletBalanceUseCase.ResposeType.FailResponse).errorMessage?.let { error ->
                WalletError(
                    error
                )
            }
    }


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

    private fun onFailConfirm(throwable: Throwable?) {
        txnConfirmMutableLiveData.value = TransferConfErrorSnkBar(GENERAL_ERROR)
    }


    fun makeTransferRequestCall(transferReqMap: HashMap<String, Any>) {

        ovoP2pTransferUseCase.transferOvo(::onSuccessTransfer, ::onFailTransfer, transferReqMap)
    }

    private fun onSuccessTransfer(ovoP2PTransferRequestBase: OvoP2pTransferRequestBase) {
        ovoP2PTransferRequestBase.ovoP2pTransferRequest?.let { reqObj ->
            reqObj.errors?.let { errList ->
                if (errList.isNotEmpty()) {
                    errList[0][Constants.Keys.MESSAGE]?.let { errMsg ->
                        setTransferErrorMessage(errMsg)
                    }
                } else {
                    setSuccessData(reqObj)
                }
            } ?: kotlin.run {
                setSuccessData(reqObj)
            }
        }
    }

    private fun setTransferErrorMessage(errMsg: String) {
        if (errMsg.contentEquals(NON_OVO_ERROR)) {
            transferReqBaseMutableLiveData.value = TransferReqNonOvo
        } else {
            transferReqBaseMutableLiveData.value = TransferReqErrorPage(errMsg)
        }
    }

    private fun setSuccessData(reqObj: OvoP2pTransferRequest) {
        transferReqBaseMutableLiveData.value =
            reqObj.dstAccName?.let { TransferReqData(it) }
    }

    private fun onFailTransfer(throwable: Throwable?) {
        transferReqBaseMutableLiveData.value = TransferReqErrorSnkBar(GENERAL_ERROR)
    }


    override fun onCleared() {
        super.onCleared()
        getWalletBalanceUseCase.cancelJobs()
        ovoTrxnConfirmationUseCase.cancelJobs()
        ovoP2pTransferUseCase.cancelJobs()
    }


    companion object {
        const val NON_OVO_ERROR = "Nomor ponsel penerima tidak terdaftar sebagai pengguna OVO."
    }


}
