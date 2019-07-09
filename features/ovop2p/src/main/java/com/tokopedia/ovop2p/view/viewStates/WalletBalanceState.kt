package com.tokopedia.ovop2p.view.viewStates

sealed class WalletBalanceState
data class Error(val errMsg: String) : WalletBalanceState()
data class WalletData(var cashBalance: String, val rawCashBalance: String)