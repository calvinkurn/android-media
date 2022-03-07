package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RevieweeShopBadge(
    @SerializedName("tooltip")
    @Expose
    var tooltip: String = "",

    @SerializedName("reputation_score")
    @Expose
    var reputationScore: String = "",

    @SerializedName("score")
    @Expose
    var score: Int = 0,

    @SerializedName("min_badge_score")
    @Expose
    var minBadgeScore: Int = 0,

    @SerializedName("reputation_badge_url")
    @Expose
    var reputationBadgeUrl: String = "",

    @SerializedName("reputation_badge")
    @Expose
    var reputationBadge: ReputationBadge = ReputationBadge()
)