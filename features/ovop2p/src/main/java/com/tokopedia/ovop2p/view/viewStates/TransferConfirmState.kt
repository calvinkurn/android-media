package com.tokopedia.ovop2p.view.viewStates

sealed class TransferConfirmState
data class TransferConfErrorPage(val errMsg: String) : TransferConfirmState()
data class TransferConfErrorSnkBar(val errMsg: String) : TransferConfirmState()
data class GoToThankYouPage(val transferId: String): TransferConfirmState()
data class OpenPinChlngWebView(val pinUrl: String): TransferConfirmState()