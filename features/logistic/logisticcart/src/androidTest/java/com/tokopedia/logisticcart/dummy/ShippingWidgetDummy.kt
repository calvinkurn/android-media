package com.tokopedia.logisticcart.dummy

import com.tokopedia.logisticCommon.data.constant.InsuranceConstant
import com.tokopedia.logisticcart.scheduledelivery.domain.model.DeliveryProduct
import com.tokopedia.logisticcart.shipping.model.InsuranceWidgetUiModel
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingWidgetCourierError
import com.tokopedia.logisticcart.shipping.model.ShippingWidgetUiModel

enum class ShippingWidgetDummyType {
    NORMAL_FLOW,
    INITIAL_STATE,
    WHITELABEL_FLOW,
    BEBAS_ONGKIR,
    NOW_2_HOUR,
    SCHELLY_WITH_RATES,
    UNAVAILABLE_COURIER,
    ERROR_PINPOINT,
    LOADING,
    SAF_ERROR
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

        ShippingWidgetDummyType.BEBAS_ONGKIR -> {
            ShippingWidgetDummyData.bebasOngkir
        }

        ShippingWidgetDummyType.NOW_2_HOUR -> {
            ShippingWidgetDummyData.now2HourWithPromo
        }

        ShippingWidgetDummyType.SCHELLY_WITH_RATES -> {
            ShippingWidgetDummyData.schellyWithRates
        }

        ShippingWidgetDummyType.UNAVAILABLE_COURIER -> ShippingWidgetDummyData.unavailableCourier
        ShippingWidgetDummyType.ERROR_PINPOINT -> ShippingWidgetDummyData.errorPinpoint
        ShippingWidgetDummyType.LOADING -> ShippingWidgetDummyData.loading
        ShippingWidgetDummyType.SAF_ERROR -> ShippingWidgetDummyData.cartError
    }
}

private object ShippingWidgetDummyData {
    val normalFlow =
        ShippingWidgetUiModel(
            cartError = false,
            loading = false,
            courierError = null,
            scheduleDeliveryUiModel = null,
            isDisableChangeCourier = false,
            voucherLogisticExists = false,
            isWhitelabel = false,
            serviceName = "Reguler",
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
            serviceName = "Instan",
            courierShipperPrice = 30000,
            whitelabelEtaText = "Estimasi Tiba 1 - 3 Feb",
            insuranceData = InsuranceWidgetUiModel(
                useInsurance = false,
                insuranceType = InsuranceConstant.INSURANCE_TYPE_OPTIONAL,
                insurancePrice = 2000.0
            )
        )

    val bebasOngkir =
        ShippingWidgetUiModel(
            cartError = false,
            loading = false,
            courierError = null,
            scheduleDeliveryUiModel = null,
            isDisableChangeCourier = false,
            voucherLogisticExists = true,
            isWhitelabel = false,
            hideShipperName = false,
            freeShippingLogo = "https://images.tokopedia.net/img/restriction-engine/bebas-ongkir/bo_reg_20k.png",
            freeShippingTitle = "(Rp0)",
            estimatedTimeArrival = "Estimasi tiba 2-4 Feb",
            insuranceData = InsuranceWidgetUiModel(
                useInsurance = true,
                insuranceType = InsuranceConstant.INSURANCE_TYPE_OPTIONAL,
                insurancePrice = 2000.0
            )
        )

    val now2HourWithPromo =
        ShippingWidgetUiModel(
            cartError = false,
            loading = false,
            courierError = null,
            scheduleDeliveryUiModel = null,
            isDisableChangeCourier = true,
            voucherLogisticExists = true,
            isWhitelabel = false,
            hideShipperName = false,
            freeShippingTitle = "2 Jam Tiba (Rp0)",
            insuranceData = InsuranceWidgetUiModel(
                useInsurance = true,
                insuranceType = InsuranceConstant.INSURANCE_TYPE_OPTIONAL,
                insurancePrice = 2000.0
            )
        )

    val schellyWithRates =
        ShippingWidgetUiModel(
            cartError = false,
            loading = false,
            courierError = null,
            isDisableChangeCourier = false,
            voucherLogisticExists = true,
            isWhitelabel = false,
            hideShipperName = false,
            freeShippingTitle = "Tiba dalam 2 jam (Rp19000 Rp39000)",
            insuranceData = InsuranceWidgetUiModel(
                useInsurance = true,
                insuranceType = InsuranceConstant.INSURANCE_TYPE_OPTIONAL,
                insurancePrice = 2000.0
            ),
            scheduleDeliveryUiModel = ScheduleDeliveryUiModel(
                isSelected = true,
                available = true,
                hidden = false,
                title = "Jadwal Lainnya",
                deliveryProduct = DeliveryProduct(
                    textEta = "Tiba hari ini, 14:00 - 16:00",
                    textFinalPrice = "Rp9.000",
                    textRealPrice = "Rp39.000",
                    finalPrice = 9000.0,
                    realPrice = 39000.0
                )
            )
        )

    val initialState = ShippingWidgetUiModel(
        courierError = ShippingWidgetCourierError.SHIPPING_NOT_SELECTED
    )

    val unavailableCourier = ShippingWidgetUiModel(
        courierError = ShippingWidgetCourierError.COURIER_UNAVAILABLE
    )

    val errorPinpoint = ShippingWidgetUiModel(
        courierError = ShippingWidgetCourierError.NEED_PINPOINT
    )

    val loading = ShippingWidgetUiModel(
        loading = true
    )

    val cartError = ShippingWidgetUiModel(
        cartError = true
    )
}
