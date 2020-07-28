package com.tokopedia.review.common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevReviewAttachment(
        @SerializedName("fullSize")
        @Expose
        val fullSize: String = "",
        @SerializedName("thumbnail")
        @Expose
        val thumbnail: String = ""
)