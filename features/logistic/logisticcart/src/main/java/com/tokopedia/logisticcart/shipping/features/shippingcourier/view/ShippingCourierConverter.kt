package com.tokopedia.logisticcart.shipping.features.shippingcourier.view

import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticcart.shipping.model.*
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

    fun convertToCourierItemData(shippingCourierUiModel: ShippingCourierUiModel): CourierItemData {
        val courierItemData = CourierItemData()
        courierItemData.shipperId = shippingCourierUiModel.productData.shipperId
        courierItemData.serviceId = shippingCourierUiModel.serviceData.serviceId
        courierItemData.shipperProductId = shippingCourierUiModel.productData.shipperProductId
        courierItemData.name = shippingCourierUiModel.productData.shipperName
        courierItemData.estimatedTimeDelivery = shippingCourierUiModel.serviceData.serviceName
        courierItemData.minEtd = shippingCourierUiModel.productData.etd.minEtd
        courierItemData.maxEtd = shippingCourierUiModel.productData.etd.maxEtd
        courierItemData.shipperPrice = shippingCourierUiModel.productData.price.price
        courierItemData.shipperFormattedPrice = shippingCourierUiModel.productData.price.formattedPrice
        courierItemData.insurancePrice = shippingCourierUiModel.productData.insurance.insurancePrice.roundToInt()
        courierItemData.insuranceType = shippingCourierUiModel.productData.insurance.insuranceType
        courierItemData.insuranceUsedType = shippingCourierUiModel.productData.insurance.insuranceUsedType
        courierItemData.insuranceUsedInfo = shippingCourierUiModel.productData.insurance.insuranceUsedInfo
        courierItemData.insuranceUsedDefault = shippingCourierUiModel.productData.insurance.insuranceUsedDefault
        courierItemData.isUsePinPoint = shippingCourierUiModel.productData.isShowMap == 1
        courierItemData.isHideChangeCourierCard = shippingCourierUiModel.serviceData.selectedShipperProductId > 0
        courierItemData.durationCardDescription = shippingCourierUiModel.serviceData.texts.textEtaSummarize
        if (!courierItemData.isUsePinPoint) {
            if (shippingCourierUiModel.productData.error != null && shippingCourierUiModel.productData.error.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED) {
                courierItemData.isUsePinPoint = true
            }
        }
        if (shippingCourierUiModel.serviceData.orderPriority != null) {
            courierItemData.now = shippingCourierUiModel.serviceData.orderPriority.now
            courierItemData.priorityPrice = shippingCourierUiModel.serviceData.orderPriority.price
            courierItemData.priorityFormattedPrice = shippingCourierUiModel.serviceData.orderPriority.formattedPrice
            courierItemData.priorityInnactiveMessage = shippingCourierUiModel.serviceData.orderPriority.inactiveMessage
            courierItemData.priorityDurationMessage = shippingCourierUiModel.serviceData.orderPriority.staticMessage.getDurationMessage()
            courierItemData.priorityFeeMessage = shippingCourierUiModel.serviceData.orderPriority.staticMessage.getFeeMessage()
            courierItemData.priorityWarningboxMessage = shippingCourierUiModel.serviceData.orderPriority.staticMessage.getWarningBoxMessage()
            courierItemData.priorityCheckboxMessage = shippingCourierUiModel.serviceData.orderPriority.staticMessage.getCheckboxMessage()
            courierItemData.priorityPdpMessage = shippingCourierUiModel.serviceData.orderPriority.staticMessage.getPdpMessage()
        }
        courierItemData.isAllowDropshiper = shippingCourierUiModel.isAllowDropshipper
        courierItemData.additionalPrice = shippingCourierUiModel.additionalFee
        courierItemData.promoCode = shippingCourierUiModel.productData.promoCode
        courierItemData.checksum = shippingCourierUiModel.productData.checkSum
        courierItemData.ut = shippingCourierUiModel.productData.unixTime
        courierItemData.blackboxInfo = shippingCourierUiModel.blackboxInfo
        courierItemData.isSelected = true
        courierItemData.preOrderModel = shippingCourierUiModel.preOrderModel

        /*on time delivery*/
        if (shippingCourierUiModel.productData.features.ontimeDeliveryGuarantee != null) {
            val otdPrev = shippingCourierUiModel.productData.features.ontimeDeliveryGuarantee
            val otd = OntimeDelivery(
                otdPrev.available,
                otdPrev.textLabel,
                otdPrev.textDetail,
                otdPrev.urlDetail,
                otdPrev.value,
                otdPrev.iconUrl
            )
            courierItemData.ontimeDelivery = otd
        }

        /*merchant voucher*/
        if (shippingCourierUiModel.productData.features.merchantVoucherProductData != null) {
            val merchantVoucherProductData = shippingCourierUiModel.productData.features.merchantVoucherProductData
            val mvc = MerchantVoucherProductModel(
                merchantVoucherProductData.isMvc,
                merchantVoucherProductData.mvcLogo,
                merchantVoucherProductData.mvcErrorMessage
            )
            courierItemData.merchantVoucherProductModel = mvc
        }

        /*cash on delivery*/
        if (shippingCourierUiModel.productData.codProductData != null) {
            val codProductData = shippingCourierUiModel.productData.codProductData
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
        if (shippingCourierUiModel.productData.estimatedTimeArrival != null) {
            courierItemData.etaText = shippingCourierUiModel.productData.estimatedTimeArrival.textEta
            courierItemData.etaErrorCode = shippingCourierUiModel.productData.estimatedTimeArrival.errorCode
        }
        return courierItemData
    }
}
