package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import kotlin.math.roundToInt

data class OrderShipment(
    val isLoading: Boolean = false,
    val isDisabled: Boolean = false,
    val serviceName: String? = null,
    val serviceId: Int? = null,
    val serviceDuration: String? = null,
    val serviceEta: String? = "",
    val whitelabelDescription: String? = "",
    val shippingEta: String? = null,
    val serviceErrorMessage: String? = null,
    val isServicePickerEnable: Boolean = false,
    var needPinpoint: Boolean = false,
    val shipperName: String? = null,
    val shipperId: Int? = null,
    val shipperProductId: Int? = null,
    val ratesId: String? = null,
    val ut: String? = "",
    val checksum: String? = "",
    val shippingPrice: Int? = null,
    val logisticPromoTickerMessage: String? = null,
    val isShowLogisticPromoTickerMessage: Boolean = true,
    val logisticPromoViewModel: LogisticPromoUiModel? = null,
    val logisticPromoShipping: ShippingCourierUiModel? = null,
    val isApplyLogisticPromo: Boolean = false,
    val shippingRecommendationData: ShippingRecommendationData? = null,
    val insurance: OrderInsurance = OrderInsurance(),
    val isHideChangeCourierCard: Boolean = false,

    // Analytics
    var hasTriggerViewMessageTracking: Boolean = false
) {
    fun isValid(): Boolean {
        return getRealShipperProductId() > 0 && !serviceName.isNullOrEmpty()
    }

    fun getRealServiceId(): Int {
        return logisticPromoShipping?.serviceData?.serviceId
            ?: serviceId.toZeroIfNull()
    }

    fun getRealShipperProductId(): Int {
        return logisticPromoShipping?.productData?.shipperProductId
            ?: shipperProductId.toZeroIfNull()
    }

    fun getRealShipperId(): Int {
        return logisticPromoShipping?.productData?.shipperId ?: shipperId.toZeroIfNull()
    }

    fun getRealRatesId(): String {
        return logisticPromoShipping?.ratesId ?: ratesId ?: ""
    }

    fun getRealUt(): String {
        return logisticPromoShipping?.productData?.unixTime ?: ut ?: ""
    }

    fun getRealChecksum(): String {
        return logisticPromoShipping?.productData?.checkSum ?: checksum ?: ""
    }

    fun getRealOriginalPrice(): Int {
        return if (isApplyLogisticPromo && logisticPromoShipping != null && logisticPromoViewModel != null) {
            logisticPromoViewModel.shippingRate
        } else {
            shippingPrice ?: 0
        }
    }

    fun getRealShippingPrice(): Int {
        return if (isApplyLogisticPromo && logisticPromoShipping != null && logisticPromoViewModel != null) {
            logisticPromoViewModel.discountedRate
        } else {
            shippingPrice ?: 0
        }
    }

    fun isUseInsurance(): Boolean {
        return insurance.isCheckInsurance && insurance.insuranceData != null
    }

    fun getRealInsurancePrice(): Int {
        return if (insurance.isCheckInsurance && insurance.insuranceData != null) {
            insurance.insuranceData.insurancePrice.roundToInt()
        } else {
            0
        }
    }

    fun getRealServiceName(): String {
        return if (isApplyLogisticPromo && logisticPromoShipping != null && logisticPromoViewModel != null) {
            logisticPromoViewModel.title
        } else {
            serviceName ?: ""
        }
    }

    fun getRealShipperName(): String {
        return if (isApplyLogisticPromo && logisticPromoShipping != null && logisticPromoViewModel != null) {
            logisticPromoViewModel.shipperName
        } else {
            shipperName ?: ""
        }
    }

    fun getRealServiceEta(): String {
        return if (isApplyLogisticPromo && logisticPromoShipping != null && logisticPromoViewModel != null) {
            logisticPromoViewModel.etaData.textEta
        } else {
            shippingEta ?: ""
        }
    }

    val promoCode: String
        get() {
            return if (isApplyLogisticPromo && logisticPromoShipping != null && logisticPromoViewModel != null) logisticPromoViewModel.promoCode else ""
        }
}

data class OrderInsurance(
    val insuranceData: InsuranceData? = null,
    var isCheckInsurance: Boolean = false
)
