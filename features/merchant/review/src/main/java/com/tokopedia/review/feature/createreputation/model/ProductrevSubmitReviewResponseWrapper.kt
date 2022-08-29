package com.tokopedia.review.feature.createreputation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProductrevSubmitReviewResponseWrapper(
    @SerializedName("productrevSubmitReviewV2")
    @Expose
    val productrevSuccessIndicator: ProductRevSuccessSubmitReview? = null
) : Serializable