package com.tokopedia.homenav.mainnav.data.pojo.review


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ProductReviewList(
    @SerializedName("inboxReviewID")
    @Expose
    val inboxReviewID: Long,
    @SerializedName("product")
    @Expose
    val product: Product,
    @SerializedName("reputationID")
    @Expose
    val reputationID: Int
)