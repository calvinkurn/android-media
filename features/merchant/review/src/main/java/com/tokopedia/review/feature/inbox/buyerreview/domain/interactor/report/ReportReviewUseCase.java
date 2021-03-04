package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.report;

import android.text.TextUtils;

import com.tokopedia.review.feature.inbox.buyerreview.data.repository.ReputationRepository;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.report.ReportReviewDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import rx.Observable;

/**
 * @author by nisie on 9/13/17.
 */

public class ReportReviewUseCase extends UseCase<ReportReviewDomain> {

    public static final int REPORT_SPAM = 1;
    public static final int REPORT_SARA = 2;
    public static final int REPORT_OTHER = 3;

    private static final String PARAM_REVIEW_ID = "element_id";
    private static final String PARAM_SHOP_ID = "shop_id";
    private static final String PARAM_REASON = "reason";
    private static final String PARAM_OTHER_REASON = "otherreason";


    private final ReputationRepository reputationRepository;

    public ReportReviewUseCase(ReputationRepository reputationRepository) {
        super();
        this.reputationRepository = reputationRepository;
    }

    @Override
    public Observable<ReportReviewDomain> createObservable(RequestParams requestParams) {
        return reputationRepository.reportReview(requestParams);
    }

    public static RequestParams getParam(String reviewId,
                                         String shopId,
                                         int reason,
                                         String otherReason) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_REVIEW_ID, reviewId);
        params.putString(PARAM_SHOP_ID, shopId);
        params.putInt(PARAM_REASON, reason);
        if (!TextUtils.isEmpty(otherReason))
            params.putString(PARAM_OTHER_REASON, otherReason);
        return params;
    }


}
