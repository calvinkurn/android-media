package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.oneclickcheckout.order.data.get.OnboardingComponentResponse

data class OrderProfile(
        val onboardingHeaderMessage: String = "",
        val onboardingComponent: OnboardingComponentResponse = OnboardingComponentResponse(),
        val hasPreference: Boolean = false,
        val profileId: Int = 0,
        val status: Int = 0,
        val enable: Boolean = true,
        val message: String = "",
        val address: OrderProfileAddress = OrderProfileAddress(),
        val payment: OrderProfilePayment = OrderProfilePayment(),
        val shipment: OrderProfileShipment = OrderProfileShipment()
)

data class OrderProfileAddress(
        val addressId: Int = 0,
        val receiverName: String = "",
        val addressName: String = "",
        val addressStreet: String = "",
        val districtId: Int = 0,
        val districtName: String = "",
        val cityId: Int = 0,
        val cityName: String = "",
        val provinceId: Int = 0,
        val provinceName: String = "",
        val phone: String = "",
        val longitude: String = "",
        val latitude: String = "",
        val postalCode: String = ""
)

data class OrderProfileShipment(
        val serviceName: String = "",
        val serviceId: Int = 0,
        val serviceDuration: String = ""
)

data class OrderProfilePayment(
        val enable: Int = 0,
        val active: Int = 0,
        val gatewayCode: String = "",
        val gatewayName: String = "",
        val image: String = "",
        val description: String = "",
        val url: String = "",
        val minimumAmount: Long = 0,
        val maximumAmount: Long = 0,
        val fee: Double = 0.0,
        val walletAmount: Long = 0,
        val metadata: String = "",
        val mdr: Float = 0f,
        val creditCard: OrderPaymentCreditCard = OrderPaymentCreditCard(),
        val errorMessage: OrderPaymentErrorMessage = OrderPaymentErrorMessage(),
        val tickerMessage: String = ""
)