package com.tokopedia.sellerorder.detail.data.model

/**
 * Created by fwidjaja on 2019-10-07.
 */
data class SomDetailPayments (
        var paymentDataUiModel: PaymentDataUiModel = PaymentDataUiModel(),
        var paymentMethodUiModel: List<PaymentMethodUiModel> = listOf(),
        var pricingData: List<PricingData> = listOf()
) {

    data class PaymentDataUiModel(
            val label: String = "",
            val value: String = "",
            val textColor: String = ""
    )

    data class PaymentMethodUiModel(
            val label: String = "",
            val value: String = ""
    )

    data class PricingData(
            val label: String = "",
            val value: String = "",
            val textColor: String = ""
    )
}