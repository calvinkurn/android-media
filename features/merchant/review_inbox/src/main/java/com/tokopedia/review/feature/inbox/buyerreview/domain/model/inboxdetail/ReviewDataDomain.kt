package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

/**
 * @author by nisie on 8/23/17.
 */
class ReviewDataDomain constructor(
    val reviewId: Long,
    val reputationId: Long,
    val reviewTitle: String,
    val reviewMessage: String,
    val reviewRating: Int,
    val reviewImageUrl: List<ImageAttachmentDomain> = listOf(),
    val reviewCreateTime: ReviewCreateTimeDomain,
    val reviewUpdateTime: ReviewUpdateTimeDomain,
    val isReviewAnonymity: Boolean,
    val reviewResponse: ReviewResponseDomain
)