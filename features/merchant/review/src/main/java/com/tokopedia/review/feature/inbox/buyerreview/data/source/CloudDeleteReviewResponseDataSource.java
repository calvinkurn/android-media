package com.tokopedia.review.feature.inbox.buyerreview.data.source;

import com.tokopedia.authentication.AuthHelper;

import com.tokopedia.review.common.util.ReviewUtil;
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.DeleteReviewResponseMapper;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.review.feature.inbox.buyerreview.network.ReputationService;
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
                        ReviewUtil.INSTANCE.convertMapObjectToString(requestParams.getParameters())))
                .map(deleteReviewResponseMapper);
    }
}
