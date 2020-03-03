package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SkipReviewMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SkipReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.network.ReputationService;
import com.tokopedia.tkpd.tkpdreputation.utils.ReputationUtil;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import rx.Observable;

/**
 * @author by nisie on 9/12/17.
 */

public class CloudSkipReviewDataSource {

    private final ReputationService reputationService;
    private final SkipReviewMapper skipReviewMapper;
    private UserSessionInterface userSession;

    public CloudSkipReviewDataSource(ReputationService reputationService,
                                     SkipReviewMapper skipReviewMapper,
                                     UserSessionInterface userSession) {
        this.reputationService = reputationService;
        this.skipReviewMapper = skipReviewMapper;
        this.userSession = userSession;
    }


    public Observable<SkipReviewDomain> skipReview(RequestParams requestParams) {
        return reputationService.getApi()
                .skipReview(AuthHelper.generateParamsNetwork(
                        userSession.getUserId(),
                        userSession.getDeviceId(),
                        ReputationUtil.convertMapObjectToString(requestParams.getParameters())
                ))
                .map(skipReviewMapper);
    }
}
