package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

/**
 * @author by nisie on 9/26/17.
 */

data class InboxReputationDetailDomain(
    var inboxReputationResponse: InboxReputationResponseWrapper.Data.Response = InboxReputationResponseWrapper.Data.Response(),
    var reviewDomain: ReviewDomain = ReviewDomain()
)