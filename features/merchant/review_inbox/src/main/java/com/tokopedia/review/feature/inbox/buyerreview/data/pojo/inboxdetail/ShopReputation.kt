package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopReputation(
    @SerializedName("tooltip")
    @Expose
    val tooltip: String = "",

    @SerializedName("reputation_score")
    @Expose
    val reputationScore: String = "",

    @SerializedName("score")
    @Expose
    val score: Int = 0,

    @SerializedName("min_badge_score")
    @Expose
    val minBadgeScore: Int = 0,

    @SerializedName("reputation_badge_url")
    @Expose
    val reputationBadgeUrl: String = "",

    @SerializedName("reputation_badge")
    @Expose
    val reputationBadge: ReputationBadge = ReputationBadge()
)