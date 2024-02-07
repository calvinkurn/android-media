package com.tokopedia.logisticcart.shipping.model

data class ShippingWidgetUiModel(
//    // renderErrorCourierState - shipmentCartItemModel.courierSelectionErrorTitle
//    val courierErrorTitle: String = "",

    // renderShippingVibrationAnimation
    var isShippingBorderRed: Boolean = false,
    // renderShippingVibrationAnimation
    var isTriggerShippingVibrationAnimation: Boolean = false,

//    // CourierItemData.etaErrorCode
//    val etaErrorCode: Int = 0,
//    // CourierItemData.etaText
//    val estimatedTimeArrival: String = "",
//
//    // Bebas ongkir & NOW Shipment
//    val hideShipperName: Boolean = false,
//    val freeShippingTitle: String = "",
//    // LogisticPromoUiModel.imageChosenFreeShipping
//    val freeShippingLogo: String = "",
//    // label
//    val logPromoDesc: String = "",
//    val voucherLogisticExists: Boolean = false,
//    var isHasShownCourierError: Boolean = false,
//    // new OFOC
//    val boOrderMessage: String = "",
//
//    // CourierItemData.estimatedTimeDelivery
//    val serviceName: String = "",
//
//    // CourierItemData.name
//    val courierName: String = "",
//    // CourierItemData.shipperPrice
//    val courierShipperPrice: Int = 0,
//    // new OFOC
//    val courierOrderMessage: String = "",
//
//    val merchantVoucher: MerchantVoucherProductModel? = null,
//    val ontimeDelivery: OntimeDelivery? = null,
//    val cashOnDelivery: CashOnDeliveryProduct? = null,
//
//    // CourierItemData.durationCardDescription
//    val whitelabelEtaText: String = "",
//
//    val scheduleDeliveryUiModel: ScheduleDeliveryUiModel? = null,
//    val insuranceData: InsuranceWidgetUiModel? = null,
//
//    // SAF error
//    val cartError: Boolean = false,
//
//    val loading: Boolean = false,
//    val courierError: ShippingWidgetCourierError? = null,
//    val isDisableChangeCourier: Boolean = false,
//    val isWhitelabel: Boolean = false,
//
//    val logisticPromoCode: String? = null,
    val state: ShippingWidgetState = ShippingWidgetState.CourierError(ShippingWidgetCourierError.SHIPPING_NOT_SELECTED)
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

sealed interface ShippingWidgetState {
    object Loading : ShippingWidgetState

    data class CartError(val title: String, val logisticPromoCode: String?) : ShippingWidgetState

    data class CourierError(val type: ShippingWidgetCourierError) : ShippingWidgetState

    data class ScheduleDeliveryShipping(
        val label2Hour: String,
        val voucherLogisticExists: Boolean,
        var isHasShownCourierError: Boolean,
        val freeShippingTitle: String,
        val courierName: String,
        val courierShipperPrice: Int,
        val scheduleDeliveryUiModel: ScheduleDeliveryUiModel?,
        // OFOC
        val boOrderMessage: String,
        val courierOrderMessage: String,
        val insuranceWidgetUiModel: InsuranceWidgetUiModel
    ) : ShippingWidgetState

    data class SingleShipping(
        val voucherLogisticExists: Boolean,
        var isHasShownCourierError: Boolean,
        val freeShippingTitle: String,
        val courierName: String,
        val courierShipperPrice: Int,
        val logPromoDesc: String,
        val insuranceWidgetUiModel: InsuranceWidgetUiModel
    ) : ShippingWidgetState

    data class FreeShipping(
        val isHideShipperName: Boolean,
        val title: String,
        val cashOnDelivery: CashOnDeliveryProduct?,
        val etaErrorCode: Int,
        val eta: String,
        val logoUrl: String,
        val insuranceWidgetUiModel: InsuranceWidgetUiModel
    ) : ShippingWidgetState

    data class WhitelabelShipping(
        val serviceName: String,
        val courierShipperPrice: Int,
        val eta: String,
        val ontimeDelivery: OntimeDelivery?,
        val insuranceWidgetUiModel: InsuranceWidgetUiModel
    ) : ShippingWidgetState

    data class NormalShipping(
        val serviceName: String,
        val courierShipperPrice: Int,
        val etaErrorCode: Int,
        val eta: String,
        val courierName: String,
        val merchantVoucherModel: MerchantVoucherProductModel?,
        val cashOnDelivery: CashOnDeliveryProduct?,
        val insuranceWidgetUiModel: InsuranceWidgetUiModel
    ) : ShippingWidgetState
}
