package com.tokopedia.ovop2p.view.viewStates

sealed class WalletBalanceState
data class WalletError(val errMsg: String) : WalletBalanceState()
data class WalletData(var cashBalance: String, val rawCashBalance: Long): WalletBalanceState()