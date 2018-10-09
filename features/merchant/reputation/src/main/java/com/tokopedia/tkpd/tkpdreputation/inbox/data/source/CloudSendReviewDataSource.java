package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendReviewValidateMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewValidateDomain;

import rx.Observable;

/**
 * @author by nisie on 8/31/17.
 */

public class CloudSendReviewDataSource {
    private final ReputationService reputationService;
    private final SendReviewValidateMapper sendReviewValidateMapper;

    public CloudSendReviewDataSource(ReputationService reputationService,
                                     SendReviewValidateMapper sendReviewValidateMapper) {
        this.reputationService = reputationService;
        this.sendReviewValidateMapper = sendReviewValidateMapper;

    }

    public Observable<SendReviewValidateDomain> sendReviewValidation(RequestParams requestParams) {
        return reputationService
                .getApi()
                .sendReviewValidate(AuthUtil.generateParamsNetwork2(
                        MainApplication.getAppContext(), requestParams.getParameters()))
                .map(sendReviewValidateMapper);
    }

    public Observable<SendReviewValidateDomain> editReviewValidation(RequestParams requestParams) {
        return reputationService
                .getApi()
                .editReviewValidate(AuthUtil.generateParamsNetwork2(
                        MainApplication.getAppContext(), requestParams.getParameters()))
                .map(sendReviewValidateMapper);
    }
}
