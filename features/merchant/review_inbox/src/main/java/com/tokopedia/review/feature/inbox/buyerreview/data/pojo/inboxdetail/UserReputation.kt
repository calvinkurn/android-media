package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserReputation(
    @SerializedName("positive")
    @Expose
    val positive: Int = 0,

    @SerializedName("neutral")
    @Expose
    val neutral: Int = 0,

    @SerializedName("negative")
    @Expose
    val negative: Int = 0,

    @SerializedName("positive_percentage")
    @Expose
    val positivePercentage: String = "",

    @SerializedName("no_reputation")
    @Expose
    val noReputation: Int = 0
)