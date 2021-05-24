package com.tokopedia.shop.score.performance.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopLevelTooltipResponse(
        @Expose
        @SerializedName("shopLevel")
        val shopLevel: ShopLevel = ShopLevel()
) {
    data class ShopLevel(
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
                @SerializedName("itemSold")
                val itemSold: Int? = 0,
                @Expose
                @SerializedName("nextUpdate")
                val nextUpdate: String? = "",
                @Expose
                @SerializedName("niv")
                val niv: Int? = 0,
                @Expose
                @SerializedName("period")
                val period: String? = "",
                @Expose
                @SerializedName("shopLevel")
                val shopLevel: Int? = 0
        )
    }
}