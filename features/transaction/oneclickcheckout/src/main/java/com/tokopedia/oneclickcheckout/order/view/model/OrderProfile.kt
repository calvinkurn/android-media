package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.oneclickcheckout.order.data.get.OnboardingComponentResponse

data class OrderProfile(
        val onboardingHeaderMessage: String = "",
        val onboardingComponent: OnboardingComponentResponse = OnboardingComponentResponse(),
        val hasPreference: Boolean = false,
        val isChangedProfile: Boolean = false,
        val profileId: Int = 0,
        val status: Int = 0,
        val enable: Boolean = true,
        val message: String = "",
        val address: OrderProfileAddress = OrderProfileAddress(),
        val payment: OrderProfilePayment = OrderProfilePayment(),
        val shipment: OrderProfileShipment = OrderProfileShipment()
) {
    fun updateInstallment(installments: List<OrderPaymentInstallmentTerm>): OrderProfile {
        return this.copy(payment = payment.copy(creditCard = payment.creditCard.copy(availableTerms = installments)))
    }
}

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
        val fee: Long = 0,
        val walletAmount: Long = 0,
        val metadata: String = "",
        val mdr: Float = 0f,
        val creditCard: OrderPaymentCreditCard = OrderPaymentCreditCard(),
        val errorMessage: OrderPaymentErrorMessage = OrderPaymentErrorMessage()
)

data class OrderPaymentErrorMessage(
        val message: String = "",
        val button: OrderPaymentErrorMessageButton = OrderPaymentErrorMessageButton()
)

data class OrderPaymentErrorMessageButton(
        val text: String = "",
        val link: String = ""
)

data class OrderPaymentCreditCard(
        val totalCards: Int = 0,
        val availableTerms: List<OrderPaymentInstallmentTerm> = emptyList(),
        val bankCode: String = "",
        val cardType: String = "",
        val isExpired: Boolean = false,
        val tncInfo: String = ""
) {
    fun getSelectedAvailableTerms(): OrderPaymentInstallmentTerm {
        return availableTerms.first { it.isSelected }
    }
}

data class OrderPaymentInstallmentTerm(
        val term: Int = 0,
        val mdr: Float = 0f,
        val mdrSubsidize: Float = 0f,
        val minAmount: Long = 0,
        var isSelected: Boolean = false,
        var fee: Double = 0.0,
        var monthlyAmount: Double = 0.0
)