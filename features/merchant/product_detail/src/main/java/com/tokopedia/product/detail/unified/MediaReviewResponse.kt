package com.tokopedia.product.detail.unified

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 05/12/23
 */
data class MediaReviewResponse(
    @SerializedName("productrevGetReviewImage")
    val data: ReviewImage = ReviewImage(),
) {
    data class ReviewImage(
        //list
        @SerializedName("list")
        val meta: Meta = Meta(),

        @SerializedName("detail")
        val detail: Detail = Detail(),
    )

    data class Detail(
        @SerializedName("review")
        val review: Review = Review(),
    )

    data class Review(
        @SerializedName("description")
        val description: String = "",
        @SerializedName("review")
        val review: String = "",
    )

    data class Meta(
        @SerializedName("imageID")
        val imageId: String = "",
    )
}
