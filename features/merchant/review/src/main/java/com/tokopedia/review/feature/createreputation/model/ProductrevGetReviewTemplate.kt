package com.tokopedia.review.feature.createreputation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetReviewTemplate(
        @SerializedName("templates")
        @Expose
        val templates: List<String> = listOf(),
        @SerializedName("hasNext")
        @Expose
        val hasNext: Boolean = false
)
