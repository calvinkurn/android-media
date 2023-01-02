package com.tokopedia.oneclickcheckout.order.data.payment

import com.google.gson.annotations.SerializedName

class PaymentFeeGqlResponse(
    @SerializedName("getPaymentFee")
    val response: PaymentFeeResponse = PaymentFeeResponse()
)

class PaymentFeeResponse(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("errors")
    val errors: List<PaymentFeeError> = emptyList(),
    @SerializedName("data")
    val data: List<PaymentFee> = emptyList()
)

class PaymentFeeError(
    @SerializedName("code")
    val code: String = "",
    @SerializedName("message")
    val message: String = ""
)

class PaymentFee(
    @SerializedName("code")
    val code: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("amount")
    val fee: Double = 0.0,
    @SerializedName("show_tooltip")
    val showTooltip: Boolean = false,
    @SerializedName("tooltip_info")
    val tooltipInfo: String = ""
)
