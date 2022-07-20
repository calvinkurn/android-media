package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain

/**
 * @author by nisie on 9/26/17.
 */
data class InboxReputationDetailDomain(
    var inboxReputationDomain: InboxReputationDomain = InboxReputationDomain(),
    var reviewDomain: ReviewDomain = ReviewDomain()
)