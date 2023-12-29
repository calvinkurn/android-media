package com.tokopedia.checkout.domain.model.platformfee

import com.google.gson.annotations.SerializedName

data class PaymentFeeGqlResponse(
    @SerializedName("getPaymentFeeCheckout")
    val response: PaymentFeeResponse = PaymentFeeResponse()
)

data class PaymentFeeResponse(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("errors")
    val errors: List<PaymentFeeError> = emptyList(),
    @SerializedName("data")
    val data: List<PaymentFee> = emptyList()
)

data class PaymentFeeError(
    @SerializedName("code")
    val code: String = "",
    @SerializedName("message")
    val message: String = ""
)

data class PaymentFee(
    @SerializedName("code")
    val code: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("fee")
    val fee: Double = 0.0,
    @SerializedName("show_tooltip")
    val showTooltip: Boolean = false,
    @SerializedName("tooltip_info")
    val tooltipInfo: String = "",
    @SerializedName("show_slashed")
    val showSlashed: Boolean = false,
    @SerializedName("slashed_fee")
    val slashedFee: Int = 0,
    @SerializedName("range_min")
    val minRange: Double = 0.0,
    @SerializedName("range_max")
    val maxRange: Double = 0.0
)
