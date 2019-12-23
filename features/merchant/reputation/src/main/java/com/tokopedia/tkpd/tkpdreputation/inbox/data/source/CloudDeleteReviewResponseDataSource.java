package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.DeleteReviewResponseMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.tkpd.tkpdreputation.utils.ReputationUtil;
import com.tokopedia.user.session.UserSessionInterface;

import rx.Observable;

/**
 * @author by nisie on 9/27/17.
 */

public class CloudDeleteReviewResponseDataSource {
    private final ReputationService reputationService;
    private final DeleteReviewResponseMapper deleteReviewResponseMapper;
    private UserSessionInterface userSession;

    public CloudDeleteReviewResponseDataSource(ReputationService reputationService,
                                               DeleteReviewResponseMapper deleteReviewResponseMapper,
                                               UserSessionInterface userSession) {
        this.reputationService = reputationService;
        this.deleteReviewResponseMapper = deleteReviewResponseMapper;
        this.userSession = userSession;
    }


    public Observable<DeleteReviewResponseDomain> deleteReviewResponse(com.tokopedia.usecase.RequestParams requestParams) {
        return reputationService.getApi()
                .deleteReviewResponse(AuthHelper.generateParamsNetwork(
                        userSession.getUserId(),
                        userSession.getDeviceId(),
                        ReputationUtil.convertMapObjectToString(requestParams.getParameters())))
                .map(deleteReviewResponseMapper);
    }
}
