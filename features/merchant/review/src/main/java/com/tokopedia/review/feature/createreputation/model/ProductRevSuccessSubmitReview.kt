package com.tokopedia.review.feature.createreputation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProductRevSuccessSubmitReview(
    @SerializedName("success")
    @Expose
    val success: Boolean = false,
    @SerializedName("feedbackID")
    @Expose
    val feedbackID: String = ""
) : Serializable