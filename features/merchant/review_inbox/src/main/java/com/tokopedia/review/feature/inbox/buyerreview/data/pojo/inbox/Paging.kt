package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Paging(
    @SerializedName("has_next")
    @Expose
    val isHasNext: Boolean = false,

    @SerializedName("has_prev")
    @Expose
    val isHasPrev: Boolean = false
)