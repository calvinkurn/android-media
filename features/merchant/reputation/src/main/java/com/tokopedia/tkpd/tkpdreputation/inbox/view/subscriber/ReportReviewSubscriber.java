package com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.domain.model.ReportReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationReport;

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
        viewListener.onErrorReportReview(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(ReportReviewDomain reportReviewDomain) {
        viewListener.removeLoadingProgress();
        if (reportReviewDomain.isSuccess()) {
            viewListener.onSuccessReportReview();
        } else {
            viewListener.onErrorReportReview(MainApplication.getAppContext().getString(
                    R.string.default_request_error_unknown
            ));

        }

    }
}
