package com.tokopedia.review.feature.ovoincentive.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductRevIncentiveOvoResponse(
    @SerializedName("ticker")
    @Expose
    var ticker: TickerResponse = TickerResponse(),
    @SerializedName("title")
    @Expose
    var title: String = "",
    @SerializedName("subtitle")
    @Expose
    var subtitle: String = "",
    @SerializedName("description")
    @Expose
    var description: String = "",
    @SerializedName("numbered_list")
    @Expose
    var numberedList: List<String> = listOf(),
    @SerializedName("cta_text")
    @Expose
    var ctaText: String = "",
    @SerializedName("amount")
    @Expose
    val amount: Int = 0,
    @SerializedName("illustration_list")
    @Expose
    val illustrations: List<Illustration>?
) {
    data class Illustration(
        @SerializedName("image_url")
        @Expose
        val imageUrl: String?,
        @SerializedName("text")
        @Expose
        val text: String?
    )
}