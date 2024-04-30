package com.tokopedia.logisticcart.utils

import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.InsuranceWidgetUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingWidgetCourierError
import com.tokopedia.logisticcart.shipping.model.ShippingWidgetState
import com.tokopedia.logisticcart.shipping.model.ShippingWidgetUiModel

object ShippingWidgetUtils {

    fun CourierItemData?.toShippingWidgetUiModel(
        cartError: Boolean,
        cartErrorTitle: String,
        needPinpoint: Boolean,
        isDisableChangeCourier: Boolean,
        hasGeolocation: Boolean,
        isLoading: Boolean,
        isHasShownCourierError: Boolean,
        isCheckInsurance: Boolean,
        isInsurance: Boolean,
        isShippingBorderRed: Boolean,
        isTriggerShippingVibrationAnimation: Boolean
    ): ShippingWidgetUiModel {
        return ShippingWidgetUiModel().copy(
            isShippingBorderRed = isShippingBorderRed,
            isTriggerShippingVibrationAnimation = isTriggerShippingVibrationAnimation,
            state = toShippingWidgetState(
                cartError,
                cartErrorTitle,
                needPinpoint,
                isDisableChangeCourier,
                hasGeolocation,
                isLoading,
                isHasShownCourierError,
                isCheckInsurance,
                isInsurance
            )
        )
    }
}

private fun CourierItemData?.toShippingWidgetState(
    cartError: Boolean,
    cartErrorTitle: String,
    needPinpoint: Boolean,
    isDisableChangeCourier: Boolean,
    hasGeolocation: Boolean,
    isLoading: Boolean,
    isHasShownCourierError: Boolean,
    isCheckInsurance: Boolean,
    isInsurance: Boolean
): ShippingWidgetState {
    if (cartError) {
        return ShippingWidgetState.CartError(
            cartErrorTitle,
            this?.selectedShipper?.logPromoCode.orEmpty()
        )
    } else if (isLoading) {
        return ShippingWidgetState.Loading
    } else if (this == null) {
        return ShippingWidgetState.CourierError(
            type = toErrorState(
                needPinpoint,
                isDisableChangeCourier,
                hasGeolocation
            )
        )
    } else {
        if (scheduleDeliveryUiModel != null) {
            return ShippingWidgetState.ScheduleDeliveryShipping(
                label2Hour = logPromoDesc.orEmpty(),
                voucherLogisticExists = !selectedShipper.logPromoCode.isNullOrEmpty(),
                isHasShownCourierError = isHasShownCourierError,
                freeShippingTitle = freeShippingChosenCourierTitle,
                boOrderMessage = boOrderMessage,
                courierName = name.orEmpty(),
                courierOrderMessage = courierOrderMessage,
                courierShipperPrice = shipperPrice,
                insuranceWidgetUiModel = toInsuranceModel(isCheckInsurance, isInsurance),
                scheduleDeliveryUiModel = scheduleDeliveryUiModel
            )
        } else if (isDisableChangeCourier) {
            return ShippingWidgetState.SingleShipping(
                voucherLogisticExists = !selectedShipper.logPromoCode.isNullOrEmpty(),
                isHasShownCourierError = isHasShownCourierError,
                freeShippingTitle = freeShippingChosenCourierTitle,
                courierName = name.orEmpty(),
                courierShipperPrice = shipperPrice,
                logPromoDesc = logPromoDesc.orEmpty(),
                insuranceWidgetUiModel = toInsuranceModel(isCheckInsurance, isInsurance)
            )
        } else if (!selectedShipper.logPromoCode.isNullOrEmpty()) {
            return ShippingWidgetState.FreeShipping(
                isHideShipperName = isHideShipperName,
                title = freeShippingChosenCourierTitle,
                cashOnDelivery = codProductData,
                etaErrorCode = etaErrorCode,
                eta = etaText.orEmpty(),
                logoUrl = freeShippingChosenImage,
                insuranceWidgetUiModel = toInsuranceModel(
                    isCheckInsurance,
                    isInsurance
                )
            )
        } else if (isHideChangeCourierCard) {
            return ShippingWidgetState.WhitelabelShipping(
                serviceName = estimatedTimeDelivery.orEmpty(),
                courierShipperPrice = shipperPrice,
                eta = durationCardDescription,
                ontimeDelivery = ontimeDelivery,
                insuranceWidgetUiModel = toInsuranceModel(isCheckInsurance, isInsurance)
            )
        } else {
            return ShippingWidgetState.NormalShipping(
                serviceName = estimatedTimeDelivery.orEmpty(),
                courierShipperPrice = shipperPrice,
                etaErrorCode = etaErrorCode,
                eta = etaText.orEmpty(),
                courierName = name.orEmpty(),
                merchantVoucherModel = merchantVoucherProductModel,
                cashOnDelivery = codProductData,
                insuranceWidgetUiModel = toInsuranceModel(isCheckInsurance, isInsurance)
            )
        }
    }
}

private fun CourierItemData.toInsuranceModel(
    isCheckInsurance: Boolean,
    isInsurance: Boolean
): InsuranceWidgetUiModel {
    return InsuranceWidgetUiModel(
        insuranceType = selectedShipper.insuranceType,
        insuranceUsedDefault = selectedShipper.insuranceUsedDefault,
        insuranceUsedInfo = selectedShipper.insuranceUsedInfo,
        insurancePrice = selectedShipper.insurancePrice.toDouble(),
        isInsurance = isInsurance,
        useInsurance = isCheckInsurance
    )
}

private fun toErrorState(
    isCustomPinpointError: Boolean,
    isDisableChangeCourier: Boolean,
    hasGeolocation: Boolean
): ShippingWidgetCourierError {
    return if (isCustomPinpointError) {
        ShippingWidgetCourierError.NEED_PINPOINT
    } else if (isDisableChangeCourier && hasGeolocation) {
        ShippingWidgetCourierError.COURIER_UNAVAILABLE
    } else {
        ShippingWidgetCourierError.SHIPPING_NOT_SELECTED
    }
}
