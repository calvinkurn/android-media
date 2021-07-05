package com.tokopedia.review.feature.inbox.buyerreview.data.source;

import com.tokopedia.authentication.AuthHelper;

import com.tokopedia.review.common.util.ReviewUtil;
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.InboxReputationDetailMapper;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewDomain;

import com.tokopedia.review.feature.inbox.buyerreview.network.ReputationService;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import rx.Observable;

/**
 * @author by nisie on 8/19/17.
 */

public class CloudInboxReputationDetailDataSource {

    private final InboxReputationDetailMapper inboxReputationDetailMapper;
    private final ReputationService reputationService;
    private UserSessionInterface userSession;

    public CloudInboxReputationDetailDataSource(
            ReputationService reputationService,
            InboxReputationDetailMapper inboxReputationDetailMapper,
            UserSessionInterface userSession) {
        this.reputationService = reputationService;
        this.inboxReputationDetailMapper = inboxReputationDetailMapper;
        this.userSession = userSession;
    }

    public Observable<ReviewDomain> getInboxReputationDetail(RequestParams requestParams) {
        return reputationService.getApi().getInboxDetail(
                AuthHelper.generateParamsNetwork(
                        userSession.getUserId(),
                        userSession.getDeviceId(),
                        ReviewUtil.INSTANCE.convertMapObjectToString(requestParams.getParameters())))
                .map(inboxReputationDetailMapper);
    }
}
