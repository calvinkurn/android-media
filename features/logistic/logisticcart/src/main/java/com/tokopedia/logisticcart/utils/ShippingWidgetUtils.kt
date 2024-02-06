package com.tokopedia.logisticcart.utils

import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.InsuranceWidgetUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingWidgetUiModel

object ShippingWidgetUtils {

    fun CourierItemData.toShippingWidgetUiModel(model: ShippingWidgetUiModel? = null): ShippingWidgetUiModel {
        return (model ?: ShippingWidgetUiModel()).copy(
            etaErrorCode = etaErrorCode,
            estimatedTimeArrival = etaText ?: "",
            hideShipperName = isHideShipperName,
            freeShippingTitle = freeShippingChosenCourierTitle,
            freeShippingLogo = freeShippingChosenImage,
            logPromoDesc = logPromoDesc ?: "",
            voucherLogisticExists = !selectedShipper.logPromoCode.isNullOrEmpty(),
            logisticPromoCode = selectedShipper.logPromoCode,
            isWhitelabel = isHideChangeCourierCard,
            boOrderMessage = boOrderMessage,
            serviceName = estimatedTimeDelivery ?: "",
            courierName = name ?: "",
            courierShipperPrice = shipperPrice,
            courierOrderMessage = courierOrderMessage,
            merchantVoucher = merchantVoucherProductModel,
            ontimeDelivery = ontimeDelivery,
            cashOnDelivery = codProductData,
            whitelabelEtaText = durationCardDescription,
            scheduleDeliveryUiModel = scheduleDeliveryUiModel,
            insuranceData = InsuranceWidgetUiModel(
                insuranceType = selectedShipper.insuranceType,
                insuranceUsedDefault = selectedShipper.insuranceUsedDefault,
                insuranceUsedInfo = selectedShipper.insuranceUsedInfo,
                insurancePrice = selectedShipper.insurancePrice.toDouble()
            )
        )
    }
}
