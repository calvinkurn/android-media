package com.tokopedia.logisticcart.shipping.model

data class SelectedShipperModel(
    var isScheduleDelivery: Boolean = false, // to check schedule delivery or not
    var scheduleDate: String = "", // schedule delivery service id
    var timeslotId: Long = 0L, // schedule delivery product id

    var shipperId: Int = 0,
    var shipperProductId: Int = 0,
    var shipperPrice: Int = 0,
    var insurancePrice: Int = 0,

    var logPromoCode: String? = null,
    var discountedRate: Int = 0,
    var shippingRate: Int = 0,
    var benefitAmount: Int = 0,
    var promoTitle: String? = null,
    var isHideShipperName: Boolean = false,
    var shipperName: String? = null,
    var etaText: String? = null,
    var etaErrorCode: Int = 0,
    var freeShippingChosenCourierTitle: String = "",
    var freeShippingMetadata: String = "",
    var benefitClass: String = "",
    var shippingSubsidy: Long = 0,
    var boCampaignId: Long = 0,

    var serviceId: Int = 0,
) {
    fun updateSelectedShipper(scheduleDeliveryUiModel: ScheduleDeliveryUiModel?) {
        scheduleDeliveryUiModel?.takeIf { it.isSelected }?.let { scheduleDelivery ->
            isScheduleDelivery = true
            scheduleDate = scheduleDelivery.scheduleDate
            timeslotId = scheduleDelivery.timeslotId
            shipperId = scheduleDelivery.deliveryProduct?.shipperId?.toInt() ?: 0
            shipperProductId = scheduleDelivery.deliveryProduct?.shipperProductId?.toInt() ?: 0
            shipperPrice = scheduleDelivery.deliveryProduct?.finalPrice?.toInt() ?: 0
            insurancePrice = scheduleDelivery.deliveryProduct?.insurancePrice?.toInt() ?: 0

            logPromoCode = scheduleDelivery.deliveryProduct?.promoCode
            discountedRate = scheduleDelivery.deliveryProduct?.finalPrice?.toInt() ?: 0
            shippingRate = scheduleDelivery.deliveryProduct?.realPrice?.toInt() ?: 0
            benefitAmount = 0
            promoTitle = ""
            isHideShipperName = true
            shipperName = ""
            etaText = scheduleDelivery.deliveryProduct?.textEta
            etaErrorCode = 0
            freeShippingChosenCourierTitle = ""
            freeShippingMetadata = scheduleDelivery.deliveryProduct?.freeShippingMetadata ?: ""
            benefitClass = ""
            shippingSubsidy = 0
            boCampaignId = 0
        }
    }
}
