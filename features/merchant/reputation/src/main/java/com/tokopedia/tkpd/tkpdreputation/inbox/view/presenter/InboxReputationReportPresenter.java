package com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.ReportReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationReport;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber.ReportReviewSubscriber;

import javax.inject.Inject;

/**
 * @author by nisie on 9/13/17.
 */

public class InboxReputationReportPresenter extends BaseDaggerPresenter<InboxReputationReport.View>
        implements InboxReputationReport.Presenter {

    private final ReportReviewUseCase reportReviewUseCase;

    @Inject
    public InboxReputationReportPresenter(ReportReviewUseCase reportReviewUseCase) {
        this.reportReviewUseCase = reportReviewUseCase;
    }

    private InboxReputationReport.View viewListener;

    @Override
    public void attachView(InboxReputationReport.View view) {
        this.viewListener = view;
    }

    @Override
    public void detachView() {
        reportReviewUseCase.unsubscribe();
    }

    @Override
    public void reportReview(String reviewId,
                             String shopId,
                             int checkedRadioButtonId,
                             String otherReason) {
        viewListener.showLoadingProgress();
        reportReviewUseCase.execute(
                ReportReviewUseCase.getParam(
                        reviewId,
                        shopId,
                        getCheckedRadio(checkedRadioButtonId),
                        otherReason
                ),
                new ReportReviewSubscriber(viewListener));

    }

    private int getCheckedRadio(int checkedRadioButtonId) {
        if (checkedRadioButtonId == R.id.report_spam) {
            return ReportReviewUseCase.REPORT_SPAM;
        } else if (checkedRadioButtonId == R.id.report_sara) {
            return ReportReviewUseCase.REPORT_SARA;
        } else {
            return ReportReviewUseCase.REPORT_OTHER;
        }
    }
}
