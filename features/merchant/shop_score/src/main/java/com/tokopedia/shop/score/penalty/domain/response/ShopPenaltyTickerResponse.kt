package com.tokopedia.shop.score.penalty.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopPenaltyTickerResponse(
    @SerializedName("GetTargetedTicker")
    val getTargetedTicker: GetTargetedTicker? = GetTargetedTicker()
)

data class GetTargetedTicker(
    @SerializedName("List")
    val tickers: List<TickerResponse> = listOf()
) {
    data class TickerResponse(
        @SerializedName("Content")
        val content: String,
        @SerializedName("Title")
        val title: String,
        @SerializedName("Action")
        @Expose
        val action: Action?,
    ) {
        data class Action(
            @SerializedName("AppURL")
            @Expose
            val appURL: String,
            @SerializedName("Label")
            @Expose
            val label: String,
            @SerializedName("Type")
            @Expose
            val type: String,
            @SerializedName("WebURL")
            @Expose
            val webURL: String
        )
    }
}
