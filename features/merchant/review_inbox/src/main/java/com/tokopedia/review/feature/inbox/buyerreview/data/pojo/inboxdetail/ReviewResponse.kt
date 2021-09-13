package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReviewResponse {
    @SerializedName("response_message")
    @Expose
    var responseMessage: String? = null

    @SerializedName("response_create_time")
    @Expose
    var responseCreateTime: ResponseCreateTime? = null

    @SerializedName("response_by")
    @Expose
    var responseBy: String? = null
}