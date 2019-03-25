package com.tokopedia.product.detail.data.model.shop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopBadge(
        @SerializedName("badge")
        @Expose
        val badge: String = "",

        @SerializedName("badge_hd")
        @Expose
        val badgeHD: String = "",

        @SerializedName("score")
        @Expose
        val score: String = "",

        @SerializedName("score_map")
        @Expose
        val scoreMap: String = ""
) {
    data class Response(
            @SerializedName("reputation_shops")
            val result: List<ShopBadge> = listOf()
    )
}