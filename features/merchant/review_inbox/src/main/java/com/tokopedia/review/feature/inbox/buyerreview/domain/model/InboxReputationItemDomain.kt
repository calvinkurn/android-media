package com.tokopedia.review.feature.inbox.buyerreview.domain.model

/**
 * @author by nisie on 8/15/17.
 */
data class InboxReputationItemDomain(
    val inboxId: Long,
    val shopId: Long,
    val userId: Long,
    val reputationId: Long,
    val orderData: OrderDataDomain,
    val revieweeData: RevieweeDataDomain,
    val reputationData: ReputationDataDomain
)