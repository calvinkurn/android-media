package com.tokopedia.logisticcart.shipping.model

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
    var boCampaignId: Long = 0,
) {
    fun updateSelectedShipper(scheduleDeliveryUiModel: ScheduleDeliveryUiModel?) {
        scheduleDeliveryUiModel?.takeIf { it.isSelected }?.let { scheduleDelivery ->
            isScheduleDelivery = true
            scheduleDate = scheduleDelivery.scheduleDate
            timeslotId = scheduleDelivery.timeslotId
            shipperId = scheduleDelivery.deliveryProduct?.shipperId?.toInt() ?: 0
            shipperProductId = scheduleDelivery.deliveryProduct?.shipperProductId?.toInt() ?: 0
            shipperPrice = scheduleDelivery.deliveryProduct?.finalPrice?.toInt() ?: 0
            serviceId = scheduleDelivery.deliveryProduct?.serviceId?.toInt() ?: 0

            insurancePrice = scheduleDelivery.deliveryProduct?.insurance?.insurancePrice ?: 0
            insuranceType = scheduleDelivery.deliveryProduct?.insurance?.insuranceType ?: 0
            insuranceUsedType = scheduleDelivery.deliveryProduct?.insurance?.insuranceUsedType ?: 0
            insuranceUsedInfo = scheduleDelivery.deliveryProduct?.insurance?.insuranceUsedInfo
            insuranceUsedDefault = scheduleDelivery.deliveryProduct?.insurance?.insuranceUsedDefault ?: 0

            logPromoCode = scheduleDelivery.deliveryProduct?.promoStacking?.promoCode
            discountedRate = scheduleDelivery.deliveryProduct?.finalPrice?.toInt() ?: 0
            shippingRate = scheduleDelivery.deliveryProduct?.realPrice?.toInt() ?: 0
            benefitAmount = 0
            promoTitle = ""
            isHideShipperName = true
            checksum = scheduleDelivery.deliveryProduct?.checksum
            ut = scheduleDelivery.deliveryProduct?.ut
            val onTimeDeliveryData = scheduleDelivery.deliveryProduct?.features?.onTimeDeliveryGuarantee
            ontimeDelivery = onTimeDeliveryData?.let {
                OntimeDelivery(
                    it.available,
                    it.textLabel,
                    it.textDetail,
                    it.urlDetail,
                    it.value,
                    it.iconUrl
                )
            }
            etaText = scheduleDelivery.deliveryProduct?.textEta
            etaErrorCode = 0
            shipperName = ""
            freeShippingChosenCourierTitle = ""
            freeShippingMetadata = scheduleDelivery.deliveryProduct?.promoStacking?.freeShippingMetadata ?: ""
            benefitClass = scheduleDelivery.deliveryProduct?.promoStacking?.benefitClass ?: ""
            shippingSubsidy = scheduleDelivery.deliveryProduct?.promoStacking?.shippingSubsidy ?: 0
            boCampaignId = scheduleDelivery.deliveryProduct?.promoStacking?.boCampaignId ?: 0
        }
    }
}
