package com.tokopedia.review.feature.inbox.buyerreview.domain.model

/**
 * @author by nisie on 8/14/17.
 */
data class InboxReputationDomain (
    val inboxReputation: List<InboxReputationItemDomain> = listOf(),
    val paging: PagingDomain = PagingDomain()
)