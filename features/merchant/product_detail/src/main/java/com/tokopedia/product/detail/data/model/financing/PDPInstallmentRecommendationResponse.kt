package com.tokopedia.product.detail.data.model.financing

import com.google.gson.annotations.SerializedName

data class PDPInstallmentRecommendationData(
        @SerializedName("data")
        val data: PDPInstallmentRecommendation = PDPInstallmentRecommendation()
)

data class PDPInstallmentRecommendation(
        @SerializedName("monthly_price")
        val monthlyPrice: Float = 0f,

        @SerializedName("os_monthly_price")
        val osMonthlyPrice: Float = 0f,

        @SerializedName("partner_code")
        val partnerCode: String = "",

        @SerializedName("subtitle")
        val subtitle: String = ""
)