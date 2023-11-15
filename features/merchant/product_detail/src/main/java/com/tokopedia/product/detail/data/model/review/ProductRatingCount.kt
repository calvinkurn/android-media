package com.tokopedia.product.detail.data.model.review

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.view.viewholder.review.ui.ReviewRatingUiModel

data class ProductRatingCount(
    @SerializedName("ratingScore")
    @Expose
    val ratingScore: String = "",
    @SerializedName("totalRating")
    @Expose
    val totalRating: String = "",
    @SerializedName("totalReviewTextAndImage")
    @Expose
    val totalReviewTextAndImage: String = "",
    @SerializedName("showRatingReview")
    @Expose
    val showRatingReview: Boolean = true,
    @SerializedName("keywords")
    @Expose
    val keywords: List<ReviewRatingKeyword> = listOf()
) {

    companion object {

        fun ProductRatingCount.asUiModel() = ReviewRatingUiModel(
            ratingScore = ratingScore,
            totalRating = totalRating,
            totalReviewAndImage = totalReviewTextAndImage,
            show = showRatingReview,
            keywords = keywords.asUiModel()
        )

        fun ReviewRatingKeyword.asUiModel() = ReviewRatingUiModel.Keyword(
            text = text,
            filter = filter
        )

        fun List<ReviewRatingKeyword>.asUiModel() = map { it.asUiModel() }
    }
}
