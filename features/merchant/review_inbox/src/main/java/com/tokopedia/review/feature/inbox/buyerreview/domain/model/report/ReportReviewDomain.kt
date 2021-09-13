package com.tokopedia.review.feature.inbox.buyerreview.domain.model.report

/**
 * @author by nisie on 9/13/17.
 */
class ReportReviewDomain {
    var isSuccess: Boolean = false
        private set
    var errorMessage: String? = null
        private set

    constructor(errorMessage: String?) {
        this.errorMessage = errorMessage
    }

    constructor(isSuccess: Int) {
        this.isSuccess = isSuccess == 1
    }
}