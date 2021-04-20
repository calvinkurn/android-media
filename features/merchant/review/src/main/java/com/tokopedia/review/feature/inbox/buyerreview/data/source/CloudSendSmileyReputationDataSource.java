package com.tokopedia.review.feature.inbox.buyerreview.data.source;

import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.review.common.util.ReviewUtil;
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.SendSmileyReputationMapper;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendSmileyReputationDomain;
import com.tokopedia.review.feature.inbox.buyerreview.network.ReputationService;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import rx.Observable;

/**
 * @author by nisie on 8/31/17.
 */

public class CloudSendSmileyReputationDataSource {
    private final ReputationService reputationService;
    private final SendSmileyReputationMapper sendSmileyReputationMapper;
    private UserSessionInterface userSession;

    public CloudSendSmileyReputationDataSource(ReputationService reputationService,
                                               SendSmileyReputationMapper sendSmileyReputationMapper,
                                               UserSessionInterface userSession) {
        this.reputationService = reputationService;
        this.sendSmileyReputationMapper = sendSmileyReputationMapper;
        this.userSession = userSession;
    }

    public Observable<SendSmileyReputationDomain> sendSmiley(RequestParams requestParams) {
        return reputationService
                .getApi()
                .sendSmiley(AuthHelper.generateParamsNetwork(
                        userSession.getUserId(),
                        userSession.getDeviceId(),
                        ReviewUtil.INSTANCE.convertMapObjectToString(requestParams.getParameters())))
                .map(sendSmileyReputationMapper);
    }
}
