package com.tokopedia.product.detail.data.model.financing

import com.google.gson.annotations.SerializedName

data class PDPInstallmentRecommendationResponse(
        @SerializedName("ft_installment_recommendation")
        val response: PDPInstallmentRecommendationData = PDPInstallmentRecommendationData()
)

data class PDPInstallmentRecommendationData(
        @SerializedName("data")
        val data: PDPInstallmentRecommendation = PDPInstallmentRecommendation(),

        @SerializedName("message")
        val message: String = ""
)

data class PDPInstallmentRecommendation(

        @SerializedName("term")
        val creditCardInstallmentTerm: Int = 0,

        @SerializedName("mdr_value")
        val mdrValue: Float = 0f,

        @SerializedName("mdr_type")
        val mdrType: String = "",

        @SerializedName("interest_rate")
        val interestRate: Float = 0f,

        @SerializedName("minimum_amount")
        val minimumAmount: Int = 0,

        @SerializedName("maximum_amount")
        val maximumAmount: Int = 0,

        @SerializedName("monthly_price")
        val monthlyPrice: Float = 0f,

        @SerializedName("os_monthly_price")
        val osMonthlyPrice: Float = 0f,

        @SerializedName("partner_code")
        val partnerCode: String = "",

        @SerializedName("partner_name")
        val partnerName: String = "",

        @SerializedName("partner_icon")
        val partnerIcon: String = ""

)