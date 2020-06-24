package com.tokopedia.review.feature.details.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetReviewDetailStatus(
        @SerializedName("editable")
        @Expose
        val editable: Boolean = false
)