package com.tokopedia.ovop2p.view.viewStates

import com.tokopedia.ovop2p.model.OvoP2pTransferThankyouBase

sealed class ThankYouPageState
data class ThankYouErrPage(val errMsg: String) : ThankYouPageState()
data class ThankYouErrSnkBar(val errMsg: String) : ThankYouPageState()
data class ThankYouSucs(val thankyouBase: OvoP2pTransferThankyouBase) : ThankYouPageState()
