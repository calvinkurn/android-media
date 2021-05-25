package com.tokopedia.seller.menu.domain.query

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
                val shopScore: Int = 0
        )
    }
}