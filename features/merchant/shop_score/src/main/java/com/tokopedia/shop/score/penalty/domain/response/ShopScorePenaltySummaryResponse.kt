package com.tokopedia.shop.score.penalty.domain.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopScorePenaltySummaryResponse(
        @Expose
        @SerializedName("shopScorePenaltySummary")
        val shopScorePenaltySummary: ShopScorePenaltySummary = ShopScorePenaltySummary()
) {
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
                val penaltyAmount: Int = 0
        )

        data class Error(
                @Expose
                @SerializedName("message")
                val message: String = ""
        )
    }
}