package com.tokopedia.chat_common.domain.pojo.productattachment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TopchatProductRating(
        @SerializedName("rating")
        @Expose
        val starCount: Int = 0,
        @SerializedName("count")
        @Expose
        val count: Int = 0,
        @SerializedName("rating_score")
        @Expose
        val score: Float = 0f
) {
}