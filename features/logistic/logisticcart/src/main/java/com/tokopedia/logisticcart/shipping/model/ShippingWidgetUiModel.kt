package com.tokopedia.logisticcart.shipping.model

data class ShippingWidgetUiModel(
    var isShippingBorderRed: Boolean = false,
    // renderShippingVibrationAnimation
    var isTriggerShippingVibrationAnimation: Boolean = false,
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
