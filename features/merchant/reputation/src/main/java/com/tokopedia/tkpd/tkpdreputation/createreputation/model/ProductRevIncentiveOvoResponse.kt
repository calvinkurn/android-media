package com.tokopedia.tkpd.tkpdreputation.createreputation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductRevIncentiveOvoResponse (
        @SerializedName("ticker")
        @Expose
        val ticker: TickerResponse = TickerResponse(),

        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("subtitle")
        @Expose
        val subtitle: String = "",

        @SerializedName("description")
        @Expose
        val description: String = "",

        @SerializedName("numbered_list")
        @Expose
        val numberedList: List<String> = listOf(),

        @SerializedName("cta_text")
        @Expose
        val ctaText: String = ""
)