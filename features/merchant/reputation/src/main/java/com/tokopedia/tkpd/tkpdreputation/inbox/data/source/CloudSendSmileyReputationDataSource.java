package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendSmileyReputationMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.SendSmileyReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.utils.ReputationUtil;
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
                        ReputationUtil.convertMapObjectToString(requestParams.getParameters())))
                .map(sendSmileyReputationMapper);
    }
}
