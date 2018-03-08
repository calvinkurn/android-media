package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.InboxReputationDetailMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.ReviewDomain;

import rx.Observable;

/**
 * @author by nisie on 8/19/17.
 */

public class CloudInboxReputationDetailDataSource {

    private final InboxReputationDetailMapper inboxReputationDetailMapper;
    private final ReputationService reputationService;

    public CloudInboxReputationDetailDataSource(
            ReputationService reputationService,
            InboxReputationDetailMapper inboxReputationDetailMapper) {
        this.reputationService = reputationService;
        this.inboxReputationDetailMapper = inboxReputationDetailMapper;
    }

    public Observable<ReviewDomain> getInboxReputationDetail(RequestParams requestParams) {
        return reputationService.getApi().getInboxDetail(
                AuthUtil.generateParamsNetwork2(
                        MainApplication.getAppContext(),
                        requestParams.getParameters()))
                .map(inboxReputationDetailMapper);
    }
}
