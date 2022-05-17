package com.tokopedia.homenav.mainnav.data.pojo.review


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ProductReviewList(
    @SerializedName("inboxReviewID")
    @Expose
    val inboxReviewID: Long = 0L,
    @SerializedName("product")
    @Expose
    val product: Product = Product(),
    @SerializedName("reputationID")
    @Expose
    val reputationID: Int = 0
)