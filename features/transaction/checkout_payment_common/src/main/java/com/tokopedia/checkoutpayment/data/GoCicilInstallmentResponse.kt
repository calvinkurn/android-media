package com.tokopedia.checkoutpayment.data

import com.google.gson.annotations.SerializedName

class GoCicilInstallmentGqlResponse(
    @SerializedName("getInstallmentInfo")
    val response: GoCicilInstallmentResponse = GoCicilInstallmentResponse()
)

class GoCicilInstallmentResponse(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("data")
    val data: GoCicilInstallmentResponseData = GoCicilInstallmentResponseData()
)

class GoCicilInstallmentResponseData(
    @SerializedName("ticker")
    val ticker: GoCicilInstallmentTickerResponse = GoCicilInstallmentTickerResponse(),
    @SerializedName("installment_options")
    val installmentOptions: List<GoCicilInstallmentOptionResponse> = emptyList()
)

class GoCicilInstallmentOptionResponse(
    @SerializedName("installment_term")
    val installmentTerm: Int = 0,
    @SerializedName("option_id")
    val optionId: String = "",
    @SerializedName("first_installment_time")
    val firstInstallmentTime: String = "",
    @SerializedName("est_installment_end")
    val estInstallmentEnd: String = "",
    @SerializedName("first_due_message")
    val firstDueMessage: String = "",
    @SerializedName("interest_amount")
    val interestAmount: Double = 0.0,
    @SerializedName("fee_amount")
    val feeAmount: Double = 0.0,
    @SerializedName("installment_amount_per_period")
    val installmentAmountPerPeriod: Double = 0.0,
    @SerializedName("label_type")
    val labelType: String = "",
    @SerializedName("label_message")
    val labelMessage: String = "",
    @SerializedName("is_active")
    val isActive: Boolean = false,
    @SerializedName("description")
    val description: String = "",
    @SerializedName("is_recommended")
    val isRecommended: Boolean = false
)

class GoCicilInstallmentTickerResponse(
    @SerializedName("message")
    val message: String = ""
)
