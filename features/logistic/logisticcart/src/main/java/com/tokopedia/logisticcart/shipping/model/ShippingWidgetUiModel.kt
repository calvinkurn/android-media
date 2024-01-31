package com.tokopedia.logisticcart.shipping.model

data class ShippingWidgetUiModel(
    // renderErrorCourierState - shipmentCartItemModel.courierSelectionErrorTitle
    val courierErrorTitle: String = "",

    // renderShippingVibrationAnimation
    var isShippingBorderRed: Boolean = false,
    // renderShippingVibrationAnimation
    var isTriggerShippingVibrationAnimation: Boolean = false,

    // CourierItemData.etaErrorCode
    val etaErrorCode: Int = 0,
    // CourierItemData.etaText
    val estimatedTimeArrival: String = "",

    // Bebas ongkir & NOW Shipment
    val hideShipperName: Boolean = false,
    val freeShippingTitle: String = "",
    // LogisticPromoUiModel.imageChosenFreeShipping
    val freeShippingLogo: String = "",
    // label
    val logPromoDesc: String = "",
    val voucherLogisticExists: Boolean = false,
    var isHasShownCourierError: Boolean = false,
    // new OFOC
    val boOrderMessage: String = "",

    // CourierItemData.estimatedTimeDelivery
    val estimatedTimeDelivery: String = "",

    // CourierItemData.name
    val courierName: String = "",
    // CourierItemData.shipperPrice
    val courierShipperPrice: Int = 0,
    // new OFOC
    val courierOrderMessage: String = "",

    val merchantVoucher: MerchantVoucherProductModel? = null,
    val ontimeDelivery: OntimeDelivery? = null,
    val cashOnDelivery: CashOnDeliveryProduct? = null,

    // CourierItemData.durationCardDescription
    val whitelabelEtaText: String = "",

    val scheduleDeliveryUiModel: ScheduleDeliveryUiModel? = null,
    val insuranceData: InsuranceWidgetUiModel? = null,

    // SAF error
    val cartError: Boolean = false,

    val loading: Boolean = false,
    val courierError: ShippingWidgetCourierError? = null,
    val isDisableChangeCourier: Boolean = false,
    val isWhitelabel: Boolean = false,

    val logisticPromoCode: String? = null
)

enum class ShippingWidgetCourierError {
    NEED_PINPOINT, COURIER_UNAVAILABLE, SHIPPING_NOT_SELECTED
}

data class InsuranceWidgetUiModel(
    var useInsurance: Boolean? = null,
    var insuranceType: Int = 0,
    var insuranceUsedDefault: Int = 0,
    var insuranceUsedInfo: String? = null,
    var insurancePrice: Double = 0.0,
    var isInsurance: Boolean = false
)
