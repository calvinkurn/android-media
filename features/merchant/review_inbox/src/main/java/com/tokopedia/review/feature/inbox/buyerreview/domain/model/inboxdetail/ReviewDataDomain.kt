package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

/**
 * @author by nisie on 8/23/17.
 */
class ReviewDataDomain constructor(
    val reviewId: Int, val reputationId: Int, val reviewTitle: String?,
    val reviewMessage: String?, val reviewRating: Int,
    reviewImageUrl: List<ImageAttachmentDomain?>?,
    reviewCreateTime: ReviewCreateTimeDomain,
    reviewUpdateTime: ReviewUpdateTimeDomain,
    reviewAnonymity: Boolean, reviewResponse: ReviewResponseDomain
) {
    val reviewImageUrl: List<ImageAttachmentDomain?>? = null
    val reviewCreateTime: ReviewCreateTimeDomain
    val reviewUpdateTime: ReviewUpdateTimeDomain
    val isReviewAnonymity: Boolean
    val reviewResponse: ReviewResponseDomain

    init {
        this.reviewImageUrl = reviewImageUrl
        this.reviewCreateTime = reviewCreateTime
        this.reviewUpdateTime = reviewUpdateTime
        isReviewAnonymity = reviewAnonymity
        this.reviewResponse = reviewResponse
    }
}