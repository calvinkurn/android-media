package com.tokopedia.logisticcart.shipping.model

data class ShippingWidgetUiModel(
    val courierErrorTitle: String = "",
    val courierErrorDescription: String = "",

    var isShippingBorderRed: Boolean = false,
    var isTriggerShippingVibrationAnimation: Boolean = false,

    val etaErrorCode: Int = 0,
    val hideShipperName: Boolean = false,
    val freeShippingTitle: String = "",
    val freeShippingEtaText: String = "",

    val normalCourierEtaText: String = "",
    val courierName: String = "",
    val courierShipperPrice: Int = 0,
    val courierEtaCode: Int = 0,
    val courierEtaText: String = "",
    val merchantVoucher: MerchantVoucherProductModel? = null,
    val ontimeDelivery: OntimeDelivery? = null,
    val urlOnTimeDelivery: String = "",
    val logPromoDesc: String = "",
    val voucherLogisticExists: Boolean = false,
    var isHasShownCourierError: Boolean = false,
    val whitelabelEta: String = "",
    val whitelabelDescription: String = "",
    val cashOnDelivery: CashOnDeliveryProduct? = null,
    val scheduleDeliveryUiModel: ScheduleDeliveryUiModel? = null
)
