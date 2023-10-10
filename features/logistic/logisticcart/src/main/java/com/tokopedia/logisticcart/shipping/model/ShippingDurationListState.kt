package com.tokopedia.logisticcart.shipping.model

sealed interface ShippingDurationListState {
    data class ShowList(val list: MutableList<RatesViewModelType>) : ShippingDurationListState
    data class NoShipmentAvailable(val message: String) : ShippingDurationListState
    data class RatesError(val e: Throwable) : ShippingDurationListState
}
