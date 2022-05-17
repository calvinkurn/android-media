package com.tokopedia.homenav.mainnav.data.pojo.review


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ReviewProduct(
    @SerializedName("productrevWaitForFeedback")
    @Expose
    val productRevWaitForFeedback: ProductRevWaitForFeedback = ProductRevWaitForFeedback()
)