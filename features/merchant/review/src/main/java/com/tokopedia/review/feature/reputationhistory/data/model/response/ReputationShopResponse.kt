package com.tokopedia.review.feature.reputationhistory.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReputationShopResponse(
    @Expose
    @SerializedName("reputation_shops")
    val reputationShops: List<ReputationShop> = listOf()
) {
    data class ReputationShop(
        @Expose
        @SerializedName("badge")
        val badge: String = "",
        @Expose
        @SerializedName("badge_hd")
        val badgeHd: String = "",
        @Expose
        @SerializedName("score")
        val score: String = ""
    )
}