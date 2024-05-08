package com.tokopedia.logisticcart.shipping.model.backup

import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.model.CashOnDeliveryProduct
import com.tokopedia.logisticcart.shipping.model.MerchantVoucherProductModel
import com.tokopedia.logisticcart.shipping.model.OntimeDelivery
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel

data class BackupShippingWidgetUiModel(
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
    // LogisticPromoUiModel.imageChosenFreeShipping
    val freeShippingLogo: String = "",
    // label
    val logPromoDesc: String = "",
    val voucherLogisticExists: Boolean = false,
    var isHasShownCourierError: Boolean = false,
    // new OFOC
    val boOrderMessage: String = "",

    // showNormalShippingCourier
    val currentAddress: RecipientAddressModel,
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
    val insuranceData: InsuranceWidgetUiModel? = null
)

data class InsuranceWidgetUiModel(
    var useInsurance: Boolean? = null,
    var insuranceType: Int = 0,
    var insuranceUsedDefault: Int = 0,
    var insuranceUsedInfo: String? = null,
    var insurancePrice: Double = 0.0,
    var isInsurance: Boolean = false
)
