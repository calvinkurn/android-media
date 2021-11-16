package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReputationBadge(
    @SerializedName("level")
    @Expose
    var level: Int = 0,

    @SerializedName("set")
    @Expose
    var set: Int = 0
)