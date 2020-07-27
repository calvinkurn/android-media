package com.tokopedia.shop.review.product.data.model.reviewlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ShopReputation {
    @SerializedName("tooltip")
    @Expose
    var tooltip: String? = null
    @SerializedName("reputation_score")
    @Expose
    var reputationScore: String? = null
    @SerializedName("score")
    @Expose
    var score = 0
    @SerializedName("min_badge_score")
    @Expose
    var minBadgeScore = 0
    @SerializedName("reputation_badge_url")
    @Expose
    var reputationBadgeUrl: String? = null
    @SerializedName("reputation_badge")
    @Expose
    var reputationBadge: ReputationBadge? = null

}