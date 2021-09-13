package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReviewInboxDatum {
    @SerializedName("product_data")
    @Expose
    var productData: ProductData? = null

    @SerializedName("review_inbox_id")
    @Expose
    var reviewInboxId: Long = 0
        private set

    @SerializedName("review_id")
    @Expose
    var reviewId: Long = 0
        private set

    @SerializedName("review_has_reviewed")
    @Expose
    var isReviewHasReviewed = false

    @SerializedName("review_is_skippable")
    @Expose
    var isReviewIsSkippable = false

    @SerializedName("review_is_skipped")
    @Expose
    var isReviewIsSkipped = false

    @SerializedName("review_is_editable")
    @Expose
    var isReviewIsEditable = false

    @SerializedName("review_data")
    @Expose
    var reviewData: ReviewData? = null
    fun setReviewInboxId(reviewInboxId: Int) {
        this.reviewInboxId = reviewInboxId.toLong()
    }

    fun setReviewId(reviewId: Int) {
        this.reviewId = reviewId.toLong()
    }
}