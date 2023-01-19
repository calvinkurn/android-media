package com.tokopedia.logisticcart.shipping.model

import kotlin.math.roundToInt

data class SelectedShipperModel(
    var isScheduleDelivery: Boolean = false, // to check schedule delivery or not
    var scheduleDate: String = "", // schedule delivery service id
    var timeslotId: Long = 0L, // schedule delivery product id

    var shipperId: Int = 0,
    var shipperProductId: Int = 0,
    var shipperPrice: Int = 0,
    var serviceId: Int = 0,

    var insurancePrice: Int = 0,
    var insuranceType: Int = 0,
    var insuranceUsedType: Int = 0,
    var insuranceUsedInfo: String? = null,
    var insuranceUsedDefault: Int = 0,

    var logPromoCode: String? = null,
    var discountedRate: Int = 0,
    var shippingRate: Int = 0,
    var benefitAmount: Int = 0,
    var promoTitle: String? = null,
    var isHideShipperName: Boolean = false,
    var checksum: String? = null,
    var ut: String? = null,
    var ontimeDelivery: OntimeDelivery? = null,
    var codProductData: CashOnDeliveryProduct? = null,
    var etaText: String? = null,
    var etaErrorCode: Int = 0,
    var shipperName: String? = null,
    var freeShippingChosenCourierTitle: String = "",
    var freeShippingMetadata: String = "",
    var benefitClass: String = "",
    var shippingSubsidy: Long = 0,
    var boCampaignId: Long = 0
) {
    fun updateSelectedShipper(scheduleDeliveryUiModel: ScheduleDeliveryUiModel?) {
        scheduleDeliveryUiModel?.takeIf { it.isSelected }?.let { scheduleDelivery ->
            isScheduleDelivery = true
            scheduleDate = scheduleDelivery.scheduleDate
            timeslotId = scheduleDelivery.timeslotId
            shipperId = scheduleDelivery.deliveryProduct.shipperId.toInt()
            shipperProductId = scheduleDelivery.deliveryProduct.shipperProductId.toInt()
            shipperPrice = scheduleDelivery.deliveryProduct.finalPrice.roundToInt()
            serviceId = scheduleDelivery.deliveryProduct.serviceId.toInt()
            insurancePrice = scheduleDelivery.deliveryProduct.insurance.insurancePrice.roundToInt()
            insuranceType = scheduleDelivery.deliveryProduct.insurance.insuranceType
            insuranceUsedType = scheduleDelivery.deliveryProduct.insurance.insuranceUsedType
            insuranceUsedInfo = scheduleDelivery.deliveryProduct.insurance.insuranceUsedInfo
            insuranceUsedDefault = scheduleDelivery.deliveryProduct.insurance.insuranceUsedDefault

            discountedRate = scheduleDelivery.deliveryProduct.finalPrice.roundToInt()
            shippingRate = scheduleDelivery.deliveryProduct.realPrice.roundToInt()
            benefitAmount = 0
            promoTitle = ""
            isHideShipperName = true
            checksum = scheduleDelivery.deliveryProduct.checksum
            ut = scheduleDelivery.deliveryProduct.ut
            val onTimeDeliveryData = scheduleDelivery.deliveryProduct.features.onTimeDeliveryGuarantee
            ontimeDelivery = onTimeDeliveryData.let {
                OntimeDelivery(
                    it.available,
                    it.textLabel ?: "",
                    it.textDetail ?: "",
                    it.urlDetail ?: "",
                    it.value,
                    it.iconUrl ?: ""
                )
            }
            etaText = scheduleDelivery.deliveryProduct.textEta
            etaErrorCode = 0
            shipperName = ""
            freeShippingChosenCourierTitle = ""
            if (scheduleDelivery.deliveryProduct.promoStacking.disabled.not()) {
                logPromoCode = scheduleDelivery.deliveryProduct.promoStacking.promoCode
                freeShippingMetadata = scheduleDelivery.deliveryProduct.promoStacking.freeShippingMetadata.toJson()
                benefitClass = scheduleDelivery.deliveryProduct.promoStacking.benefitClass
                shippingSubsidy = scheduleDelivery.deliveryProduct.promoStacking.freeShippingMetadata.shippingSubsidy
                boCampaignId = scheduleDelivery.deliveryProduct.promoStacking.boCampaignId
            }
        }
    }
}
