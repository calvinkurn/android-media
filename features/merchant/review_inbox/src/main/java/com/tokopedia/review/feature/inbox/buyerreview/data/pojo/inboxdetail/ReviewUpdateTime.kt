package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReviewUpdateTime(
    @SerializedName("date_time_fmt1")
    @Expose
    var dateTimeFmt1: String = ""
)