package com.tokopedia.tradein.viewmodel.liveState

sealed class TradeInHomeState
data class GoToCheckout(val deviceId: String?, val price: String) : TradeInHomeState()
data class GoToHargaFinal(val imei: String?) : TradeInHomeState()
