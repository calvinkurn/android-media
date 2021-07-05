package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData

data class OrderShipment(
        val serviceName: String? = null,
        val serviceId: Int? = null,
        val serviceDuration: String? = null,
        val serviceEta: String? = null,
        val shippingEta: String? = null,
        val serviceErrorMessage: String? = null,
        val isServicePickerEnable: Boolean = false,
        val needPinpoint: Boolean = false,
        val shipperName: String? = null,
        val shipperId: Int? = null,
        val shipperProductId: Int? = null,
        val ratesId: String? = null,
        val ut: String? = null,
        val checksum: String? = null,
        val shippingPrice: Int? = null,
        val logisticPromoTickerMessage: String? = null,
        val logisticPromoViewModel: LogisticPromoUiModel? = null,
        val logisticPromoShipping: ShippingCourierUiModel? = null,
        val isApplyLogisticPromo: Boolean = false,
        val shippingRecommendationData: ShippingRecommendationData? = null,
        val insuranceData: InsuranceData? = null,
        val isCheckInsurance: Boolean = false
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
        } else shippingPrice ?: 0
    }

    fun getRealShippingPrice(): Int {
        return if (isApplyLogisticPromo && logisticPromoShipping != null && logisticPromoViewModel != null) {
            logisticPromoViewModel.discountedRate
        } else shippingPrice ?: 0
    }

    fun getRealInsurancePrice(): Int {
        return if (isCheckInsurance && insuranceData != null) {
            insuranceData.insurancePrice
        } else 0
    }
}