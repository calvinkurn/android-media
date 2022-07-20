package com.tokopedia.review.feature.inbox.buyerreview.domain.model

/**
 * @author by nisie on 8/15/17.
 */
data class InboxReputationItemDomain(
    val inboxId: String = "",
    val shopId: String = "",
    val userId: String = "",
    val reputationId: String = "",
    val orderData: OrderDataDomain,
    val revieweeData: RevieweeDataDomain,
    val reputationData: ReputationDataDomain
)