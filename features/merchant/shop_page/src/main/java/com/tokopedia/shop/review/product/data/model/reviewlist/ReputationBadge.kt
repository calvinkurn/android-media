package com.tokopedia.shop.review.product.data.model.reviewlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReputationBadge {
    @SerializedName("level")
    @Expose
    var level = 0
    @SerializedName("set")
    @Expose
    var set = 0

}