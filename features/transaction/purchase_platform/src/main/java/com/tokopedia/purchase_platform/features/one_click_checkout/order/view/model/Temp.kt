package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model

import com.tokopedia.logisticcart.shipping.model.LogisticPromoViewModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.InsuranceData

data class OrderPreferenceTemp(
        val id: Int = 0,
        val address: Address = Address(),
        val shipment: Shipment = Shipment(),
        val payment: Payment = Payment(),
        val isLoadingRates: Boolean = false
)

data class Shipment(
        val serviceName: String? = null,
        val serviceId: Int? = null,
        val serviceDuration: String? = null,
        val serviceErrorMessage: String? = null,
        val isServicePickerEnable: Boolean = false,
        val shipperName: String? = null,
        val shipperProductId: Int? = null,
        val logisticPromoTickerMessage: String? = null,
        val logisticPromoViewModel: LogisticPromoViewModel? = null, // BBO ?
        val shippingRecommendationData: ShippingRecommendationData? = null
)

data class Payment(
        val image: String? = null,
        val description: String? = null,
        val gatewayCode: String? = null,
        val url: String? = null,
        val gatewayName: String? = null,
        val errorMessage: String? = null
)

data class Address(
        val latitude: String? = null,
        val addressId: Int? = null,
        val addressName: String? = null,
        val provinceName: String? = null,
        val districtName: String? = null,
        val cityName: String? = null,
        val provinceId: Int? = null,
        val phone: String? = null,
        val addressStreet: String? = null,
        val receiverName: String? = null,
        val districtId: Int? = null,
        val postalCode: String? = null,
        val cityId: Int? = null,
        val longitude: String? = null
)

data class Insurance(
        val insuranceData: InsuranceData? = null
)

data class OrderTotal(
        val subTotal: Long = 0,
        val buttonState: ButtonBayarState = ButtonBayarState.DISABLE,
        val isButtonChoosePayment: Boolean = false
)

enum class ButtonBayarState { NORMAL, LOADING, DISABLE }