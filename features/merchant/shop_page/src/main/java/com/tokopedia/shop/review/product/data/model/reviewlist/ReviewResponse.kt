package com.tokopedia.shop.review.product.data.model.reviewlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReviewResponse {
    @SerializedName("response_message")
    @Expose
    var responseMessage: String? = null
    @SerializedName("response_time")
    @Expose
    var responseTime: ResponseTime? = null

}