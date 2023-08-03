package com.tokopedia.logisticcart.shipping.features.shippingcourier.view

import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticcart.scheduledelivery.domain.model.ScheduleDeliveryData
import com.tokopedia.logisticcart.shipping.model.CashOnDeliveryProduct
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.MerchantVoucherProductModel
import com.tokopedia.logisticcart.shipping.model.OntimeDelivery
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * Created by Irfan Khoirul on 08/08/18.
 */
class ShippingCourierConverter @Inject constructor() {

    fun updateSelectedCourier(courierModels: List<ShippingCourierUiModel>?, spId: Int) {
        if (courierModels != null) {
            for (courierModel in courierModels) {
                courierModel.isSelected = courierModel.productData.shipperProductId == spId
            }
        }
    }

    @JvmOverloads
    fun convertToCourierItemData(
        shippingCourierUiModel: ShippingCourierUiModel?,
        shippingRecommendationData: ShippingRecommendationData? = null,
        shipmentCartItemModel: ShipmentCartItemModel? = null
    ): CourierItemData {
        val courierItemData = CourierItemData()
        shippingCourierUiModel?.let {
            courierItemData.shipperId = it.productData.shipperId
            courierItemData.serviceId = it.serviceData.serviceId
            courierItemData.shipperProductId = it.productData.shipperProductId
            courierItemData.name = it.productData.shipperName
            courierItemData.estimatedTimeDelivery = it.serviceData.serviceName
            courierItemData.minEtd = it.productData.etd.minEtd
            courierItemData.maxEtd = it.productData.etd.maxEtd
            courierItemData.shipperPrice = it.productData.price.price
            courierItemData.shipperFormattedPrice = it.productData.price.formattedPrice
            courierItemData.insurancePrice = it.productData.insurance.insurancePrice.roundToInt()
            courierItemData.insuranceType = it.productData.insurance.insuranceType
            courierItemData.insuranceUsedType = it.productData.insurance.insuranceUsedType
            courierItemData.insuranceUsedInfo = it.productData.insurance.insuranceUsedInfo
            courierItemData.insuranceUsedDefault = it.productData.insurance.insuranceUsedDefault
            courierItemData.isUsePinPoint = it.productData.isShowMap == 1
            courierItemData.isHideChangeCourierCard = it.serviceData.selectedShipperProductId > 0
            courierItemData.durationCardDescription = it.serviceData.texts.textEtaSummarize
            if (!courierItemData.isUsePinPoint) {
                if (it.productData.error.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED) {
                    courierItemData.isUsePinPoint = true
                }
            }
            courierItemData.now = it.serviceData.orderPriority.now
            courierItemData.priorityPrice = it.serviceData.orderPriority.price
            courierItemData.priorityFormattedPrice = it.serviceData.orderPriority.formattedPrice
            courierItemData.priorityInnactiveMessage = it.serviceData.orderPriority.inactiveMessage
            courierItemData.priorityDurationMessage = it.serviceData.orderPriority.staticMessage.durationMessage
            courierItemData.priorityFeeMessage = it.serviceData.orderPriority.staticMessage.feeMessage
            courierItemData.priorityWarningboxMessage = it.serviceData.orderPriority.staticMessage.warningBoxMessage
            courierItemData.priorityCheckboxMessage = it.serviceData.orderPriority.staticMessage.checkboxMessage
            courierItemData.priorityPdpMessage = it.serviceData.orderPriority.staticMessage.pdpMessage
            courierItemData.isAllowDropshiper = it.isAllowDropshipper
            courierItemData.additionalPrice = it.additionalFee
            courierItemData.promoCode = it.productData.promoCode
            courierItemData.checksum = it.productData.checkSum
            courierItemData.ut = it.productData.unixTime
            courierItemData.blackboxInfo = it.blackboxInfo
            courierItemData.isSelected = true
            courierItemData.preOrderModel = it.productShipmentDetailModel?.preOrderModel

            /*on time delivery*/
            val otdPrev = shippingCourierUiModel.productData.features.ontimeDeliveryGuarantee
            val otd = OntimeDelivery(
                otdPrev.available,
                otdPrev.textLabel,
                otdPrev.textDetail,
                otdPrev.urlDetail,
                otdPrev.value,
                otdPrev.iconUrl,
                otdPrev.urlText
            )
            courierItemData.ontimeDelivery = otd

            /*merchant voucher*/
            val merchantVoucherProductData = it.productData.features.merchantVoucherProductData
            val mvc = MerchantVoucherProductModel(
                merchantVoucherProductData.isMvc,
                merchantVoucherProductData.mvcLogo,
                merchantVoucherProductData.mvcErrorMessage
            )
            courierItemData.merchantVoucherProductModel = mvc

            /*cash on delivery*/
            val codProductData = it.productData.codProductData
            val codProduct = CashOnDeliveryProduct(
                codProductData.isCodAvailable,
                codProductData.codText,
                codProductData.codPrice.roundToInt(),
                codProductData.formattedPrice,
                codProductData.tncText,
                codProductData.tncLink
            )
            courierItemData.codProductData = codProduct

            /*ETA*/
            courierItemData.etaText = it.productData.estimatedTimeArrival.textEta
            courierItemData.etaErrorCode = it.productData.estimatedTimeArrival.errorCode
        }

        /*Schedule Delivery*/
        courierItemData.setScheduleDeliveryUiModel(
            scheduleDeliveryData = shippingRecommendationData?.scheduleDeliveryData,
            validationMetadata = shipmentCartItemModel?.validationMetadata
        )

        return courierItemData
    }

    fun convertToCourierItemDataNew(
        shippingCourierUiModel: ShippingCourierUiModel?,
        shippingRecommendationData: ShippingRecommendationData? = null,
        validationMetadata: String = ""
    ): CourierItemData {
        val courierItemData = CourierItemData()
        shippingCourierUiModel?.let {
            courierItemData.shipperId = it.productData.shipperId
            courierItemData.serviceId = it.serviceData.serviceId
            courierItemData.shipperProductId = it.productData.shipperProductId
            courierItemData.name = it.productData.shipperName
            courierItemData.estimatedTimeDelivery = it.serviceData.serviceName
            courierItemData.minEtd = it.productData.etd.minEtd
            courierItemData.maxEtd = it.productData.etd.maxEtd
            courierItemData.shipperPrice = it.productData.price.price
            courierItemData.shipperFormattedPrice = it.productData.price.formattedPrice
            courierItemData.insurancePrice = it.productData.insurance.insurancePrice.roundToInt()
            courierItemData.insuranceType = it.productData.insurance.insuranceType
            courierItemData.insuranceUsedType = it.productData.insurance.insuranceUsedType
            courierItemData.insuranceUsedInfo = it.productData.insurance.insuranceUsedInfo
            courierItemData.insuranceUsedDefault = it.productData.insurance.insuranceUsedDefault
            courierItemData.isUsePinPoint = it.productData.isShowMap == 1
            courierItemData.isHideChangeCourierCard = it.serviceData.selectedShipperProductId > 0
            courierItemData.durationCardDescription = it.serviceData.texts.textEtaSummarize
            if (!courierItemData.isUsePinPoint) {
                if (it.productData.error != null && it.productData.error.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED) {
                    courierItemData.isUsePinPoint = true
                }
            }
            if (it.serviceData.orderPriority != null) {
                courierItemData.now = it.serviceData.orderPriority.now
                courierItemData.priorityPrice = it.serviceData.orderPriority.price
                courierItemData.priorityFormattedPrice = it.serviceData.orderPriority.formattedPrice
                courierItemData.priorityInnactiveMessage = it.serviceData.orderPriority.inactiveMessage
                courierItemData.priorityDurationMessage = it.serviceData.orderPriority.staticMessage.durationMessage
                courierItemData.priorityFeeMessage = it.serviceData.orderPriority.staticMessage.feeMessage
                courierItemData.priorityWarningboxMessage = it.serviceData.orderPriority.staticMessage.warningBoxMessage
                courierItemData.priorityCheckboxMessage = it.serviceData.orderPriority.staticMessage.checkboxMessage
                courierItemData.priorityPdpMessage = it.serviceData.orderPriority.staticMessage.pdpMessage
            }
            courierItemData.isAllowDropshiper = it.isAllowDropshipper
            courierItemData.additionalPrice = it.additionalFee
            courierItemData.promoCode = it.productData.promoCode
            courierItemData.checksum = it.productData.checkSum
            courierItemData.ut = it.productData.unixTime
            courierItemData.blackboxInfo = it.blackboxInfo
            courierItemData.isSelected = true
            courierItemData.preOrderModel = it.preOrderModel

            /*on time delivery*/
            if (shippingCourierUiModel.productData.features.ontimeDeliveryGuarantee != null) {
                val otdPrev = shippingCourierUiModel.productData.features.ontimeDeliveryGuarantee
                val otd = OntimeDelivery(
                    otdPrev.available,
                    otdPrev.textLabel,
                    otdPrev.textDetail,
                    otdPrev.urlDetail,
                    otdPrev.value,
                    otdPrev.iconUrl,
                    otdPrev.urlText
                )
                courierItemData.ontimeDelivery = otd
            }

            /*merchant voucher*/
            if (it.productData.features.merchantVoucherProductData != null) {
                val merchantVoucherProductData = it.productData.features.merchantVoucherProductData
                val mvc = MerchantVoucherProductModel(
                    merchantVoucherProductData.isMvc,
                    merchantVoucherProductData.mvcLogo,
                    merchantVoucherProductData.mvcErrorMessage
                )
                courierItemData.merchantVoucherProductModel = mvc
            }

            /*cash on delivery*/
            if (it.productData.codProductData != null) {
                val codProductData = it.productData.codProductData
                val codProduct = CashOnDeliveryProduct(
                    codProductData.isCodAvailable,
                    codProductData.codText,
                    codProductData.codPrice.roundToInt(),
                    codProductData.formattedPrice,
                    codProductData.tncText,
                    codProductData.tncLink
                )
                courierItemData.codProductData = codProduct
            }

            /*ETA*/
            if (it.productData.estimatedTimeArrival != null) {
                courierItemData.etaText = it.productData.estimatedTimeArrival.textEta
                courierItemData.etaErrorCode = it.productData.estimatedTimeArrival.errorCode
            }
        }

        /*Schedule Delivery*/
        courierItemData.setScheduleDeliveryUiModel(
            scheduleDeliveryData = shippingRecommendationData?.scheduleDeliveryData,
            validationMetadata = validationMetadata
        )

        return courierItemData
    }

    private fun CourierItemData.setScheduleDeliveryUiModel(
        scheduleDeliveryData: ScheduleDeliveryData?,
        validationMetadata: String?
    ) {
        scheduleDeliveryData?.takeIf { it.hidden.not() }
            ?.apply {
                scheduleDeliveryUiModel = convertToScheduleDeliveryUiModel(
                    validationMetadata ?: ""
                )
            }
    }

    fun convertToCourierItemDataWithPromo(shippingCourierUiModel: ShippingCourierUiModel, data: LogisticPromoUiModel): CourierItemData {
        val courierData = convertToCourierItemData(shippingCourierUiModel)
        courierData.logPromoCode = data.promoCode
        courierData.logPromoMsg = data.disableText
        courierData.discountedRate = data.discountedRate
        courierData.shippingRate = data.shippingRate
        courierData.benefitAmount = data.benefitAmount
        courierData.promoTitle = data.title
        courierData.isHideShipperName = data.hideShipperName
        courierData.shipperName = data.shipperName
        courierData.etaText = data.etaData.textEta
        courierData.etaErrorCode = data.etaData.errorCode
        courierData.freeShippingChosenCourierTitle = data.freeShippingChosenCourierTitle
        courierData.freeShippingMetadata = data.freeShippingMetadata
        courierData.benefitClass = data.benefitClass
        courierData.shippingSubsidy = data.shippingSubsidy
        courierData.boCampaignId = data.boCampaignId
        return courierData
    }

    private fun ScheduleDeliveryData.convertToScheduleDeliveryUiModel(
        validationMetadata: String
    ): ScheduleDeliveryUiModel {
        return ScheduleDeliveryUiModel(
            isSelected = recommend && available,
            available = available,
            ratesId = ratesId,
            hidden = hidden,
            title = title,
            text = text,
            notice = notice,
            deliveryServices = deliveryServices
        ).apply {
            setScheduleDateAndTimeslotId(
                validationMetadata = validationMetadata
            )
        }
    }
}
