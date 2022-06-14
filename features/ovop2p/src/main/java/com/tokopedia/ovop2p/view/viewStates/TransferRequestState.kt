package com.tokopedia.ovop2p.view.viewStates

sealed class TransferRequestState
data class TransferReqErrorPage(val errMsg: String) : TransferRequestState()
data class TransferReqErrorSnkBar(val errMsg: String) : TransferRequestState()
object TransferReqNonOvo : TransferRequestState()
data class TransferReqData(var dstAccName: String): TransferRequestState()