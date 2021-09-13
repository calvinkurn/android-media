package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RevieweeBuyerBadge {
    @SerializedName("positive")
    @Expose
    var positive = 0

    @SerializedName("neutral")
    @Expose
    var neutral = 0

    @SerializedName("negative")
    @Expose
    var negative = 0

    @SerializedName("positive_percentage")
    @Expose
    var positivePercentage: String? = null

    @SerializedName("no_reputation")
    @Expose
    var noReputation = 0
}