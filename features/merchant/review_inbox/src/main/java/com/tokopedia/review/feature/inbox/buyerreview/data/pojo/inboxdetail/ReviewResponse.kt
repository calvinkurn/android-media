package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    @SerializedName("response_message")
    @Expose
    val responseMessage: String = "",

    @SerializedName("response_create_time")
    @Expose
    val responseCreateTime: ResponseCreateTime = ResponseCreateTime(),

    @SerializedName("response_by")
    @Expose
    val responseBy: String = ""
)