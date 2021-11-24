package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

/**
 * @author by nisie on 8/19/17.
 */
class ReviewItemDomain constructor(
    val productData: ProductDataDomain, val reviewInboxId: Long,
    val reviewId: Long, val isReviewHasReviewed: Boolean,
    val isReviewIsSkippable: Boolean, val isReviewIsSkipped: Boolean,
    val isReviewIsEditable: Boolean, val reviewData: ReviewDataDomain
)