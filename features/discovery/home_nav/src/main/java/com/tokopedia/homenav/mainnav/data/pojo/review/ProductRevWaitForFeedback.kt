package com.tokopedia.homenav.mainnav.data.pojo.review


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ProductRevWaitForFeedback(
    @SerializedName("list")
    @Expose
    val list: List<ProductReviewList> = listOf(),
    @SerializedName("hasNext")
    @Expose
    val hasNext: Boolean = false
)