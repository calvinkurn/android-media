package com.tokopedia.chat_common.domain.pojo.productattachment

import com.google.gson.annotations.SerializedName

data class TopchatProductRating(
    @SerializedName("rating")
    val starCount: Int = 0,
    @SerializedName("count")
    val count: Int = 0,
    @SerializedName("rating_score")
    val score: Float = 0f
)
