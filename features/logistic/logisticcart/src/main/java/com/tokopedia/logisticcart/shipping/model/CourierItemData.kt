package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 25/01/18.
 */
@Parcelize
data class CourierItemData(
    var shipperId: Int = 0,
    var shipperProductId: Int = 0,
    var serviceId: Int = 0,
    var name: String? = null,
    var serviceName: String? = null,
    var deliverySchedule: String? = null,
    var estimatedTimeDelivery: String? = null,
    var minEtd: Int = 0,
    var maxEtd: Int = 0,
    var shipperPrice: Int = 0,
    var shipperFormattedPrice: String? = null,
    var insurancePrice: Int = 0,
    var additionalPrice: Int = 0,
    var courierInfo: String? = null,
    var insuranceType: Int = 0,
    var insuranceUsedType: Int = 0,
    var insuranceUsedInfo: String? = null,
    var insuranceUsedDefault: Int = 0,
    var isUsePinPoint: Boolean = false,
    var isAllowDropshiper: Boolean = false,
    var isSelected: Boolean = false,
    var shipmentItemDataEtd: String? = null,
    var shipmentItemDataType: String? = null,
    var promoCode: String? = null,
    var logPromoCode: String? = null,
    var logPromoMsg: String? = null,
    var discountedRate: Int = 0,
    var shippingRate: Int = 0,
    var benefitAmount: Int = 0,
    var promoTitle: String? = null,
    var isHideShipperName: Boolean = false,
    var logPromoDesc: String? = null,
    var checksum: String? = null,
    var ut: String? = null,
    var blackboxInfo: String? = null,
    var now: Boolean? = null,
    var priorityPrice: Int = 0,
    var priorityInnactiveMessage: String? = null,
    var priorityFormattedPrice: String? = null,
    var priorityInactiveMessage: String? = null,
    var priorityDurationMessage: String? = null,
    var priorityCheckboxMessage: String? = null,
    var priorityWarningboxMessage: String? = null,
    var priorityFeeMessage: String? = null,
    var priorityPdpMessage: String? = null,
    var ontimeDelivery: OntimeDelivery? = null,
    var codProductData: CashOnDeliveryProduct? = null,
    var etaText: String? = null,
    var etaErrorCode: Int = 0,
    var shipperName: String? = null,
    var merchantVoucherProductModel: MerchantVoucherProductModel? = null,
    var preOrderModel: PreOrderModel? = null,
    var freeShippingChosenCourierTitle: String = "",
    var freeShippingChosenImage: String = "",
    var isHideChangeCourierCard: Boolean = false,
    var durationCardDescription: String = "",
    var freeShippingMetadata: String = "",
    var benefitClass: String = "",
    var shippingSubsidy: Long = 0,
    var boCampaignId: Long = 0,
    var scheduleDeliveryUiModel: ScheduleDeliveryUiModel? = null
) : Parcelable, ShipmentOptionData {

    val selectedShipper: SelectedShipperModel
        get() = getSelectedShipperData()

    private fun getSelectedShipperData(): SelectedShipperModel {
        return SelectedShipperModel(
            shipperId = shipperId,
            shipperProductId = shipperProductId,
            shipperPrice = shipperPrice,
            serviceId = serviceId,
            insurancePrice = insurancePrice,
            insuranceType = insuranceType,
            insuranceUsedType = insuranceUsedType,
            insuranceUsedInfo = insuranceUsedInfo,
            insuranceUsedDefault = insuranceUsedDefault,
            logPromoCode = logPromoCode,
            discountedRate = discountedRate,
            shippingRate = shippingRate,
            benefitAmount = benefitAmount,
            promoTitle = promoTitle,
            isHideShipperName = isHideShipperName,
            checksum = checksum,
            ut = ut,
            ontimeDelivery = ontimeDelivery,
            codProductData = codProductData,
            etaText = etaText,
            etaErrorCode = etaErrorCode,
            shipperName = shipperName,
            freeShippingChosenCourierTitle = freeShippingChosenCourierTitle,
            freeShippingMetadata = freeShippingMetadata,
            benefitClass = benefitClass,
            shippingSubsidy = shippingSubsidy,
            boCampaignId = boCampaignId
        ).apply {
            updateSelectedShipper(scheduleDeliveryUiModel)
        }
    }

    companion object {
        fun clone(courierItemData: CourierItemData, scheduleDeliveryUiModel: ScheduleDeliveryUiModel): CourierItemData {
            return courierItemData.copy(scheduleDeliveryUiModel = scheduleDeliveryUiModel)
        }
    }
}
