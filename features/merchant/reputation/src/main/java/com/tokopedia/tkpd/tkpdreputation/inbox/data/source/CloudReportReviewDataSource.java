package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.ReportReviewMapper;
import com.tokopedia.tkpd.tkpdreputation.domain.model.ReportReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.utils.ReputationUtil;
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
                        ReputationUtil.convertMapObjectToString(requestParams.getParameters())
                ))
                .map(reportReviewMapper);
    }
}
