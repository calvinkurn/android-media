package com.tokopedia.review.feature.createreputation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetReviewTemplateResponseWrapper(
        @SerializedName("productrevGetListPersonalizedReviewTemplate")
        @Expose
        val productrevGetPersonalizedReviewTemplate: ProductrevGetReviewTemplate = ProductrevGetReviewTemplate()
)