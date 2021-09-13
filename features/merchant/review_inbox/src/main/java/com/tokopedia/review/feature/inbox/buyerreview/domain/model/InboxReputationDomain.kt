package com.tokopedia.review.feature.inbox.buyerreview.domain.model

/**
 * @author by nisie on 8/14/17.
 */
class InboxReputationDomain constructor(
    inboxReputation: List<InboxReputationItemDomain?>?,
    paging: PagingDomain
) {
    val inboxReputation: List<InboxReputationItemDomain?>? = null
    val paging: PagingDomain

    init {
        this.inboxReputation = inboxReputation
        this.paging = paging
    }
}