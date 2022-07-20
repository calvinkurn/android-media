package com.tokopedia.homenav.mainnav.data.pojo.review


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ProductReviewList(
    @SerializedName("product")
    @Expose
    val product: Product = Product(),
    @SerializedName("reputationIDStr")
    @Expose
    val reputationIDStr: String = ""
)