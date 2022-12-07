package com.tokopedia.review.feature.bulk_write_review.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BulkReviewSubmitRequestParam(
    @SerializedName("inboxID")
    @Expose
    val inboxID: String,
    @SerializedName("reputationID")
    @Expose
    val reputationID: String,
    @SerializedName("productID")
    @Expose
    val productID: String,
    @SerializedName("shopID")
    @Expose
    val shopID: String,
    @SerializedName("rating")
    @Expose
    val rating: Int,
    @SerializedName("reviewText")
    @Expose
    val reviewText: String,
    @SerializedName("isAnonymous")
    @Expose
    val isAnonymous: Boolean,
    @SerializedName("attachmentIDs")
    @Expose
    val attachmentIDs: List<String>,
    @SerializedName("utmSource")
    @Expose
    val utmSource: String,
    @SerializedName("badRatingCategoryIDs")
    @Expose
    val badRatingCategoryIDs: List<String>,
    @SerializedName("videoAttachments")
    @Expose
    val videoAttachments: List<VideoAttachment>,
    @SerializedName("orderID")
    @Expose
    val orderID: String
) {
    data class VideoAttachment(
        @SerializedName("uploadID")
        @Expose
        val uploadID: String,
        @SerializedName("url")
        @Expose
        val url: String
    )
}
