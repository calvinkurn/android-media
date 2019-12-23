package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendReviewValidateMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewValidateDomain;
import com.tokopedia.tkpd.tkpdreputation.utils.ReputationUtil;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import rx.Observable;

/**
 * @author by nisie on 8/31/17.
 */

public class CloudSendReviewDataSource {
    private final ReputationService reputationService;
    private final SendReviewValidateMapper sendReviewValidateMapper;
    private UserSessionInterface userSession;

    public CloudSendReviewDataSource(ReputationService reputationService,
                                     SendReviewValidateMapper sendReviewValidateMapper,
                                     UserSessionInterface userSession) {
        this.reputationService = reputationService;
        this.sendReviewValidateMapper = sendReviewValidateMapper;
        this.userSession = userSession;
    }

    public Observable<SendReviewValidateDomain> sendReviewValidation(com.tokopedia.usecase.RequestParams requestParams) {
        return reputationService
                .getApi()
                .sendReviewValidate(AuthHelper.generateParamsNetwork(
                        userSession.getUserId(),
                        userSession.getDeviceId(),
                        ReputationUtil.convertMapObjectToString(requestParams.getParameters())
                ))
                .map(sendReviewValidateMapper);
    }

    public Observable<SendReviewValidateDomain> editReviewValidation(RequestParams requestParams) {
        return reputationService
                .getApi()
                .editReviewValidate(AuthHelper.generateParamsNetwork(
                        userSession.getUserId(),
                        userSession.getDeviceId(),
                        ReputationUtil.convertMapObjectToString(requestParams.getParameters())
                ))
                .map(sendReviewValidateMapper);
    }
}
