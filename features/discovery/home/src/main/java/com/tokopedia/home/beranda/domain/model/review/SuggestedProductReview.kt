package com.tokopedia.home.beranda.domain.model.review


import com.google.gson.annotations.SerializedName

data class SuggestedProductReview(
    @SerializedName("suggestedProductReview")
    val suggestedProductReview: SuggestedProductReviewResponse = SuggestedProductReviewResponse()
)