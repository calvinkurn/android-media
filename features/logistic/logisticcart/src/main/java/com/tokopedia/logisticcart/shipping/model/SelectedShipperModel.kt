package com.tokopedia.logisticcart.shipping.model

data class SelectedShipperModel(
    var isScheduleDelivery: Boolean = false, // to check schedule delivery or not
    var scheduleDate: String = "", // schedule delivery service id
    var timeslotId: Long = 0L, // schedule delivery product id

    var shipperId: Int = 0,
    var shipperProductId: Int = 0,
    var shipperPrice: Int = 0,
    var promoCode: String? = null,
    var freeShippingMetadata: String = "",
    var etaText: String? = null,

    val serviceId: Int = 0,
    val shippingRate: Int = 0,
    val benefitClass: String = "",
    val boCampaignId: Long = 0,
    val shippingSubsidy: Long = 0,
) {
    fun updateSelectedShipper(scheduleDeliveryUiModel: ScheduleDeliveryUiModel?) {
        scheduleDeliveryUiModel?.takeIf { it.isSelected }?.let { scheduleDelivery ->
            isScheduleDelivery = true
            scheduleDate = scheduleDelivery.scheduleDate
            timeslotId = scheduleDelivery.timeslotId
            shipperId = scheduleDelivery.deliveryProduct?.shipperId?.toInt() ?: 0
            shipperProductId = scheduleDelivery.deliveryProduct?.shipperProductId?.toInt() ?: 0
            shipperPrice = scheduleDelivery.deliveryProduct?.finalPrice?.toInt() ?: 0
            promoCode = scheduleDelivery.deliveryProduct?.promoCode
            freeShippingMetadata = scheduleDelivery.deliveryProduct?.freeShippingMetadata ?: ""
            etaText = scheduleDelivery.deliveryProduct?.textEta
        }
    }
}
