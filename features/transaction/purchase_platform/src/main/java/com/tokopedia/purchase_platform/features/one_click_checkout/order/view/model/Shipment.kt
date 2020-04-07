package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model

import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.InsuranceData

data class Shipment(
        val serviceName: String? = null,
        val serviceId: Int? = null,
        val serviceDuration: String? = null,
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
    fun getRealShipperProductId(): Int {
        return logisticPromoShipping?.productData?.shipperProductId ?: shipperProductId.toZeroIfNull()
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
}