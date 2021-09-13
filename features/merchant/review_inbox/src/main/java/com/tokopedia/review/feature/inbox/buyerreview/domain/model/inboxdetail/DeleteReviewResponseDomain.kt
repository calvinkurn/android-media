package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

/**
 * @author by nisie on 9/27/17.
 */
class DeleteReviewResponseDomain constructor(isSuccess: Int) {
    val isSuccess: Boolean

    init {
        this.isSuccess = isSuccess == 1
    }
}