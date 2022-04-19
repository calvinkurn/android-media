package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

/**
 * @author by nisie on 8/23/17.
 */
class ReviewResponseDomain (
    val responseMessage: String,
    val responseCreateTime: ResponseCreateTimeDomain,
    val responseBy: String?
)