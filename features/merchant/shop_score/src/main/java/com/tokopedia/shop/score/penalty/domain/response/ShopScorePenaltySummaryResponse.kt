package com.tokopedia.shop.score.penalty.domain.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY

data class ShopScorePenaltySummary(
    @Expose
    @SerializedName("error")
    val error: Error = Error(),
    @Expose
    @SerializedName("result")
    val result: Result = Result()
) {
    data class Result(
        @Expose
        @SerializedName("penalty")
        val penalty: Int = 0,
        @Expose
        @SerializedName("penaltyAmount")
        val penaltyAmount: Int = 0,
        @SerializedName("penaltyDynamic")
        val penaltyDynamic: Int = 0,
        @SerializedName("orderVerified")
        val orderVerified: Long = 0,
        @SerializedName("shopLevel")
        val shopLevel: Int = 0,
        @SerializedName("penaltyCumulativePercentage")
        val penaltyCumulativePercentage: Float = 0.0f,
        @SerializedName("penaltyCumulativePercentageFormatted")
        val penaltyCumulativePercentageFormatted: String = String.EMPTY,
        @SerializedName("conversionData")
        val conversionData: List<ConversionData> = listOf()
    ) {

        data class ConversionData(
            @SerializedName("cumulativePercentage")
            val cumulativePercentage: Float = 0.0f,
            @SerializedName("cumulativePercentageFormatted")
            val cumulativePercentageFormatted: String = String.EMPTY,
            @SerializedName("penaltyPoint")
            val penaltyPoint: Long = 0,
            @SerializedName("conversionRateFlag")
            val conversionRateFlag: Boolean = false
        )

    }

    data class Error(
        @Expose
        @SerializedName("message")
        val message: String = ""
    )
}
