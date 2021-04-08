package com.tokopedia.shop.score.performance.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReputationShopResponse(
        @Expose
        @SerializedName("reputation_shops")
        val reputationShops: List<ReputationShop> = listOf()
) {
    data class ReputationShop(
            @Expose
            @SerializedName("score")
            val score: String = ""
    )
}