package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReviewInboxDatum(
    @SerializedName("product_data")
    @Expose
    val productData: ProductData = ProductData(),

    @SerializedName("review_inbox_id")
    @Expose
    val reviewInboxId: String = "",

    @SerializedName("review_id")
    @Expose
    val reviewId: String = "",

    @SerializedName("review_has_reviewed")
    @Expose
    val isReviewHasReviewed: Boolean = false,

    @SerializedName("review_is_skippable")
    @Expose
    val isReviewIsSkippable: Boolean = false,

    @SerializedName("review_is_skipped")
    @Expose
    val isReviewIsSkipped: Boolean = false,

    @SerializedName("review_is_editable")
    @Expose
    val isReviewIsEditable: Boolean = false,

    @SerializedName("review_data")
    @Expose
    val reviewData: ReviewData = ReviewData()

)