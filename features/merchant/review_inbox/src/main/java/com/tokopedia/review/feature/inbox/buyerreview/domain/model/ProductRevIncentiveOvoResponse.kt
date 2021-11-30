package com.tokopedia.review.feature.inbox.buyerreview.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductRevIncentiveOvoResponse (
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
        var ctaText: String = ""
)