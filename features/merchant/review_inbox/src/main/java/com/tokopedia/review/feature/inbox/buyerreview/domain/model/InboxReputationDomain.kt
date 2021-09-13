package com.tokopedia.review.feature.inbox.buyerreview.domain.model

/**
 * @author by nisie on 8/14/17.
 */
class InboxReputationDomain constructor(
    val inboxReputation: List<InboxReputationItemDomain> = listOf(),
    val paging: PagingDomain = PagingDomain()
)