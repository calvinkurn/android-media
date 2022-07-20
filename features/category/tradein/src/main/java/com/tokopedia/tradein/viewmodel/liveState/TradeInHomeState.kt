package com.tokopedia.tradein.viewmodel.liveState

sealed class TradeInHomeState
data class GoToCheckout(val imei: String?, val displayName: String, val price: String) : TradeInHomeState()
