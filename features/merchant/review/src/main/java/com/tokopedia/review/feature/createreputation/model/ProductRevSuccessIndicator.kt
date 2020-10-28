package com.tokopedia.review.feature.createreputation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductRevSuccessIndicator(
        @SerializedName("success")
        @Expose
        val success: Boolean = false
)