package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 8/19/17.
 */
class ReviewItemDomain constructor(
    @SerializedName("product") @Expose val productData: ProductDataDomain,
    @SerializedName("reviewIdStr") @Expose val reviewId: String,
    @SerializedName("hasReviewed") @Expose val isReviewHasReviewed: Boolean,
    @SerializedName("isSkipped") @Expose val isReviewIsSkipped: Boolean,
    @SerializedName("isEditable") @Expose val isReviewIsEditable: Boolean,
    @SerializedName("reviewData") @Expose val reviewData: ReviewDataDomain
)