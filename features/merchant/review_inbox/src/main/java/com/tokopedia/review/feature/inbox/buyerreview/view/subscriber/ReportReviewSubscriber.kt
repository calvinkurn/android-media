package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.report.ReportReviewDomain
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationReport
import rx.Subscriber

/**
 * @author by nisie on 9/13/17.
 */
class ReportReviewSubscriber constructor(private val viewListener: InboxReputationReport.View) :
    Subscriber<ReportReviewDomain>() {

    override fun onCompleted() {}

    override fun onError(e: Throwable) {
        viewListener.removeLoadingProgress()
        viewListener.onErrorReportReview(viewListener.getErrorMessage(e))
    }

    override fun onNext(reportReviewDomain: ReportReviewDomain) {
        viewListener.removeLoadingProgress()
        if (reportReviewDomain.isSuccess) {
            viewListener.onSuccessReportReview()
        } else {
            viewListener.onErrorReportReview(reportReviewDomain.errorMessage)
        }
    }
}