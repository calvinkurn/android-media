package com.tokopedia.catalog.model.raw


import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class CatalogProductReviewResponse(
    @Expose @SerializedName("catalogGetProductReview")
    val catalogGetProductReview: CatalogGetProductReview?
) {
    data class CatalogGetProductReview(
        @Expose @SerializedName("header")
        val header: Header?,
        @Expose @SerializedName("reviewData")
        val reviewData: ReviewData?
    ) {
        data class Header(
            @Expose @SerializedName("code")
            val code: Int?,
            @Expose @SerializedName("message")
            val message: String?
        )

        data class ReviewData(
            @Expose @SerializedName("avgRating")
            val avgRating: String?,
            @Expose @SerializedName("reviews")
            val reviews: List<Review?>?,
            @Expose @SerializedName("totalHelpfulReview")
            val totalHelpfulReview: String?
        ) {
            @Parcelize
            data class Review(
                @Expose @SerializedName("informativeScore")
                val informativeScore: Int?,
                @Expose @SerializedName("rating")
                val rating: Int?,
                @Expose @SerializedName("reviewDate")
                val reviewDate: String?,
                @Expose @SerializedName("reviewImageUrl")
                val reviewImageUrl: List<String?>?,
                @Expose @SerializedName("reviewText")
                val reviewText: String?,
                @Expose @SerializedName("reviewerName")
                val reviewerName: String?
            ) : Parcelable
        }
    }
}