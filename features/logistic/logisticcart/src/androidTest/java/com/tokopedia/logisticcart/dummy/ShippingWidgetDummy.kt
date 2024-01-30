package com.tokopedia.logisticcart.dummy

import com.tokopedia.logisticCommon.data.constant.InsuranceConstant
import com.tokopedia.logisticcart.shipping.model.InsuranceWidgetUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingWidgetUiModel

enum class ShippingWidgetDummyType {
    NORMAL_FLOW, INITIAL_STATE, WHITELABEL_FLOW
}

fun String.toDummyType(): ShippingWidgetDummyType {
    return ShippingWidgetDummyType.valueOf(this)
}

fun ShippingWidgetDummyType.toDummyData(): ShippingWidgetUiModel {
    return when (this) {
        ShippingWidgetDummyType.NORMAL_FLOW -> {
            ShippingWidgetDummyData.normalFlow
        }

        ShippingWidgetDummyType.INITIAL_STATE -> {
            ShippingWidgetDummyData.initialState
        }

        ShippingWidgetDummyType.WHITELABEL_FLOW -> {
            ShippingWidgetDummyData.whitelabelFlow
        }
    }
}

object ShippingWidgetDummyData {
    val normalFlow =
        ShippingWidgetUiModel(
            cartError = false,
            loading = false,
            courierError = null,
            scheduleDeliveryUiModel = null,
            isDisableChangeCourier = false,
            voucherLogisticExists = false,
            isWhitelabel = false,
            estimatedTimeDelivery = "Reguler",
            courierName = "Kurir Rekomendasi",
            courierShipperPrice = 11500,
            etaErrorCode = 0,
            estimatedTimeArrival = "Estimasi Tiba 1 - 3 Feb",
            cashOnDelivery = null,
            insuranceData = InsuranceWidgetUiModel(
                useInsurance = true,
                insuranceType = InsuranceConstant.INSURANCE_TYPE_MUST,
                insurancePrice = 2000.0
            )
        )

    val whitelabelFlow =
        ShippingWidgetUiModel(
            cartError = false,
            loading = false,
            courierError = null,
            scheduleDeliveryUiModel = null,
            isDisableChangeCourier = false,
            voucherLogisticExists = false,
            isWhitelabel = true,
            estimatedTimeDelivery = "Instan",
            courierShipperPrice = 30000,
            whitelabelEtaText = "Estimasi Tiba 1 - 3 Feb",
            insuranceData = InsuranceWidgetUiModel(
                useInsurance = false,
                insuranceType = InsuranceConstant.INSURANCE_TYPE_OPTIONAL,
                insurancePrice = 2000.0
            )
        )

    val initialState = ShippingWidgetUiModel()

}
