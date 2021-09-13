package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Paging {
    @SerializedName("has_next")
    @Expose
    var isHasNext = false

    @SerializedName("has_prev")
    @Expose
    var isHasPrev = false
}