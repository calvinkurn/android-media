package com.tokopedia.review.feature.inbox.buyerreview.data.source;

import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.review.common.util.ReviewUtil;
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.ReportReviewMapper;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.report.ReportReviewDomain;
import com.tokopedia.review.feature.inbox.buyerreview.network.ReputationService;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import rx.Observable;

/**
 * @author by nisie on 9/13/17.
 */

public class CloudReportReviewDataSource {
    private final ReputationService reputationService;
    private final ReportReviewMapper reportReviewMapper;
    private UserSessionInterface userSession;

    public CloudReportReviewDataSource(ReputationService reputationService,
                                       ReportReviewMapper reportReviewMapper,
                                       UserSessionInterface userSession) {
        this.reputationService = reputationService;
        this.reportReviewMapper = reportReviewMapper;
        this.userSession = userSession;
    }


    public Observable<ReportReviewDomain> reportReview(RequestParams requestParams) {
        return reputationService.getApi()
                .reportReview(AuthHelper.generateParamsNetwork(
                        userSession.getUserId(),
                        userSession.getDeviceId(),
                        ReviewUtil.INSTANCE.convertMapObjectToString(requestParams.getParameters())
                ))
                .map(reportReviewMapper);
    }
}
