package com.tokopedia.shop.review.product.data.model.reviewlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserReputation {
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