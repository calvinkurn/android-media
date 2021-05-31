package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber;

import com.tokopedia.review.common.util.ReviewErrorHandler;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.report.ReportReviewDomain;
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationReport;

import rx.Subscriber;

/**
 * @author by nisie on 9/13/17.
 */

public class ReportReviewSubscriber extends Subscriber<ReportReviewDomain> {
    private final InboxReputationReport.View viewListener;

    public ReportReviewSubscriber(InboxReputationReport.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.removeLoadingProgress();
        viewListener.onErrorReportReview(ReviewErrorHandler.getErrorMessage(viewListener.getContext().getApplicationContext(), e));
    }

    @Override
    public void onNext(ReportReviewDomain reportReviewDomain) {
        viewListener.removeLoadingProgress();
        if (reportReviewDomain.isSuccess()) {
            viewListener.onSuccessReportReview();
        } else {
            viewListener.onErrorReportReview(reportReviewDomain.getErrorMessage());
        }

    }
}
