package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.InboxReputationDetailMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.ReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.utils.ReputationUtil;
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
                        ReputationUtil.convertMapObjectToString(requestParams.getParameters())))
                .map(inboxReputationDetailMapper);
    }
}
