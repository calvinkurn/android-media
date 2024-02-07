package com.tokopedia.logisticcart.dummy

import com.tokopedia.logisticCommon.data.constant.InsuranceConstant
import com.tokopedia.logisticcart.scheduledelivery.domain.model.DeliveryProduct
import com.tokopedia.logisticcart.shipping.model.InsuranceWidgetUiModel
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingWidgetCourierError
import com.tokopedia.logisticcart.shipping.model.ShippingWidgetState
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
            state = ShippingWidgetState.NormalShipping(
                serviceName = "Reguler",
                courierName = "Kurir Rekomendasi",
                courierShipperPrice = 11500,
                etaErrorCode = 0,
                eta = "Estimasi Tiba 1 - 3 Feb",
                cashOnDelivery = null,
                insuranceWidgetUiModel = InsuranceWidgetUiModel(
                    useInsurance = true,
                    insuranceType = InsuranceConstant.INSURANCE_TYPE_MUST,
                    insurancePrice = 2000.0
                ),
                merchantVoucherModel = null
            )
        )

    val whitelabelFlow =
        ShippingWidgetUiModel(
            state = ShippingWidgetState.WhitelabelShipping(
                serviceName = "Instan",
                courierShipperPrice = 30000,
                eta = "Estimasi Tiba 1 - 3 Feb",
                insuranceWidgetUiModel = InsuranceWidgetUiModel(
                    useInsurance = false,
                    insuranceType = InsuranceConstant.INSURANCE_TYPE_OPTIONAL,
                    insurancePrice = 2000.0
                ),
                ontimeDelivery = null
            )
        )

    val bebasOngkir =
        ShippingWidgetUiModel(
            state = ShippingWidgetState.FreeShipping(
                isHideShipperName = false,
                logoUrl = "https://images.tokopedia.net/img/restriction-engine/bebas-ongkir/bo_reg_20k.png",
                title = "(Rp0)",
                eta = "Estimasi tiba 2-4 Feb",
                insuranceWidgetUiModel = InsuranceWidgetUiModel(
                    useInsurance = true,
                    insuranceType = InsuranceConstant.INSURANCE_TYPE_OPTIONAL,
                    insurancePrice = 2000.0
                ),
                cashOnDelivery = null,
                etaErrorCode = 0
            )
        )

    val now2HourWithPromo =
        ShippingWidgetUiModel(
            state = ShippingWidgetState.SingleShipping(
                freeShippingTitle = "2 Jam Tiba (Rp0)",
                insuranceWidgetUiModel = InsuranceWidgetUiModel(
                    useInsurance = true,
                    insuranceType = InsuranceConstant.INSURANCE_TYPE_OPTIONAL,
                    insurancePrice = 2000.0
                ),

                voucherLogisticExists = true,
                courierName = "2 Jam Tiba",
                courierShipperPrice = 24000,
                isHasShownCourierError = false,
                logPromoDesc = ""
            )
        )

    val schellyWithRates =
        ShippingWidgetUiModel(
            state = ShippingWidgetState.ScheduleDeliveryShipping(
                freeShippingTitle = "Tiba dalam 2 jam (Rp19000 Rp39000)",
                insuranceWidgetUiModel = InsuranceWidgetUiModel(
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
                ),
                boOrderMessage = "",
                courierOrderMessage = "",
                courierName = "2 Jam Tiba",
                courierShipperPrice = 24000,
                isHasShownCourierError = false,
                label2Hour = "",
                voucherLogisticExists = true
            )

        )

    val initialState = ShippingWidgetUiModel(
        state = ShippingWidgetState.CourierError(
            type = ShippingWidgetCourierError.SHIPPING_NOT_SELECTED
        )
    )

    val unavailableCourier = ShippingWidgetUiModel(
        state = ShippingWidgetState.CourierError(
            type = ShippingWidgetCourierError.COURIER_UNAVAILABLE
        )
    )

    val errorPinpoint = ShippingWidgetUiModel(
        state = ShippingWidgetState.CourierError(
            type = ShippingWidgetCourierError.NEED_PINPOINT
        )

    )

    val loading = ShippingWidgetUiModel(
        state = ShippingWidgetState.Loading
    )

    val cartError = ShippingWidgetUiModel(
        state = ShippingWidgetState.CartError(
            title = "Pengiriman tidak tersedia",
            logisticPromoCode = ""
        )
    )
}
