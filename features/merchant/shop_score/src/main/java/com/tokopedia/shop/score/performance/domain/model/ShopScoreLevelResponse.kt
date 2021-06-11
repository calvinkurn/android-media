package com.tokopedia.shop.score.performance.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopScoreLevelResponse(
        @Expose
        @SerializedName("shopScoreLevel")
        val shopScoreLevel: ShopScoreLevel = ShopScoreLevel()
) {
    data class ShopScoreLevel(
            @Expose
            @SerializedName("error")
            val error: Error = Error(),
            @Expose
            @SerializedName("result")
            val result: Result = Result()
    ) {
        data class Error(
                @Expose
                @SerializedName("message")
                val message: String = ""
        )
        data class Result(
                @Expose
                @SerializedName("shopLevel")
                val shopLevel: Int = 0,
                @Expose
                @SerializedName("shopScore")
                val shopScore: Int = 0,
                @Expose
                @SerializedName("period")
                val period: String = "",
                @Expose
                @SerializedName("nextUpdate")
                val nextUpdate: String = "",
                @Expose
                @SerializedName("shopScoreDetail")
                val shopScoreDetail: List<ShopScoreDetail> = listOf()
        ) {
            data class ShopScoreDetail(
                    @Expose
                    @SerializedName("colorText")
                    val colorText: String = "",
                    @Expose
                    @SerializedName("identifier")
                    val identifier: String = "",
                    @Expose
                    @SerializedName("nextMinValue")
                    val nextMinValue: Double = 0.0,
                    @Expose
                    @SerializedName("rawValue")
                    val rawValue: Double = 0.0,
                    @Expose
                    @SerializedName("title")
                    val title: String = ""
            )
        }
    }
}