package com.tokopedia.logisticcart.shipping.model

import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.InsuranceData

data class ShippingWidgetUiModel(
    // renderErrorCourierState - shipmentCartItemModel.courierSelectionErrorTitle
    val courierErrorTitle: String = "",
    // renderErrorCourierState - shipmentCartItemModel.courierSelectionErrorDescription
    val courierErrorDescription: String = "",

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
    // Now Shipment
    // label
    val logPromoDesc: String = "",
    val voucherLogisticExists: Boolean = false,
    var isHasShownCourierError: Boolean = false,

    // showNormalShippingCourier
    val currentAddress: RecipientAddressModel,
    // CourierItemData.estimatedTimeDelivery
    val estimatedTimeDelivery: String = "",

    // CourierItemData.name
    val courierName: String = "",
    // CourierItemData.shipperPrice
    val courierShipperPrice: Int = 0,

    val merchantVoucher: MerchantVoucherProductModel? = null,
    val ontimeDelivery: OntimeDelivery? = null,
    val cashOnDelivery: CashOnDeliveryProduct? = null,

    // CourierItemData.durationCardDescription
    val whitelabelEtaText: String = "",

    val scheduleDeliveryUiModel: ScheduleDeliveryUiModel? = null,
    val insuranceData: InsuranceData = InsuranceData()
)
