package com.tokopedia.review.feature.inbox.buyerreview.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.report.ReportReviewUseCase
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationReport
import com.tokopedia.review.feature.inbox.buyerreview.view.subscriber.ReportReviewSubscriber
import com.tokopedia.review.inbox.R
import javax.inject.Inject

/**
 * @author by nisie on 9/13/17.
 */
class InboxReputationReportPresenter @Inject constructor(private val reportReviewUseCase: ReportReviewUseCase) :
    BaseDaggerPresenter<InboxReputationReport.View?>(), InboxReputationReport.Presenter {
    private var viewListener: InboxReputationReport.View? = null
    override fun attachView(view: InboxReputationReport.View) {
        viewListener = view
    }

    override fun detachView() {
        reportReviewUseCase.unsubscribe()
    }

    override fun reportReview(
        reviewId: String?,
        shopId: String?,
        checkedRadioButtonId: Int,
        otherReason: String?
    ) {
        viewListener!!.showLoadingProgress()
        reportReviewUseCase.execute(
            ReportReviewUseCase.getParam(
                reviewId,
                shopId,
                getCheckedRadio(checkedRadioButtonId),
                otherReason
            ),
            ReportReviewSubscriber(viewListener)
        )
    }

    private fun getCheckedRadio(checkedRadioButtonId: Int): Int {
        if (checkedRadioButtonId == R.id.report_spam) {
            return ReportReviewUseCase.REPORT_SPAM
        } else if (checkedRadioButtonId == R.id.report_sara) {
            return ReportReviewUseCase.REPORT_SARA
        } else {
            return ReportReviewUseCase.REPORT_OTHER
        }
    }
}