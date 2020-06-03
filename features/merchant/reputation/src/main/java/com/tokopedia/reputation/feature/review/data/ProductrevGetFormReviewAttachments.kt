package com.tokopedia.reputation.feature.review.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetFormReviewAttachments(
        @SerializedName("thumbnailURL")
        @Expose
        val thumbnailUrl: String = "",
        @SerializedName("fullsizeURL")
        @Expose
        val fullSizeUrl: String = ""
)