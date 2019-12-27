package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendReviewSubmitMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewSubmitDomain;
import com.tokopedia.tkpd.tkpdreputation.network.ReputationService;
import com.tokopedia.tkpd.tkpdreputation.utils.ReputationUtil;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import rx.Observable;

/**
 * @author by nisie on 9/5/17.
 */

public class CloudSendReviewSubmitDataSource {

    private final ReputationService reputationService;
    private final SendReviewSubmitMapper sendReviewSubmitMapper;
    private UserSessionInterface userSession;

    public CloudSendReviewSubmitDataSource(ReputationService reputationService,
                                           SendReviewSubmitMapper sendReviewSubmitMapper,
                                           UserSessionInterface userSession) {
        this.reputationService = reputationService;
        this.sendReviewSubmitMapper = sendReviewSubmitMapper;
        this.userSession = userSession;
    }

    public Observable<SendReviewSubmitDomain> sendReviewSubmit(com.tokopedia.usecase.RequestParams requestParams) {
        return reputationService.getApi()
                .sendReviewSubmit(AuthHelper.generateParamsNetwork(
                        userSession.getUserId(),
                        userSession.getDeviceId(),
                        ReputationUtil.convertMapObjectToString(requestParams.getParameters())
                ))
                .map(sendReviewSubmitMapper);
    }

    public Observable<SendReviewSubmitDomain> editReviewSubmit(RequestParams requestParams) {
        return reputationService.getApi()
                .editReviewSubmit(AuthHelper.generateParamsNetwork(
                        userSession.getUserId(),
                        userSession.getDeviceId(),
                        ReputationUtil.convertMapObjectToString(requestParams.getParameters())
                ))
                .map(sendReviewSubmitMapper);
    }
}
