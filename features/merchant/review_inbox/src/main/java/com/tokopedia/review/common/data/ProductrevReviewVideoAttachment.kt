package com.tokopedia.review.common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevReviewVideoAttachment(
    @SerializedName("url")
    @Expose
    val url: String? = null
)
