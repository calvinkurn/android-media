package com.tokopedia.checkout.data.model.request.saf

class ShipmentAddressFormRequest(
    val isOneClickShipment: Boolean,
    val isTradeIn: Boolean,
    val isSkipUpdateOnboardingState: Boolean,
    val cornerId: String?,
    val deviceId: String?,
    val leasingId: String?,
    val isPlusSelected: Boolean,
    val isCheckoutReimagine: Boolean
)
