package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

/**
 * @author by nisie on 8/31/17.
 */
class SendSmileyReputationDomain constructor(isSuccess: Int) {
    val isSuccess: Boolean

    init {
        this.isSuccess = isSuccess == 1
    }
}