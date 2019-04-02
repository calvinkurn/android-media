package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.DeleteReviewResponseMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain;

import rx.Observable;

/**
 * @author by nisie on 9/27/17.
 */

public class CloudDeleteReviewResponseDataSource {
    private final ReputationService reputationService;
    private final DeleteReviewResponseMapper deleteReviewResponseMapper;

    public CloudDeleteReviewResponseDataSource(ReputationService reputationService,
                                               DeleteReviewResponseMapper deleteReviewResponseMapper) {
        this.reputationService = reputationService;
        this.deleteReviewResponseMapper = deleteReviewResponseMapper;
    }


    public Observable<DeleteReviewResponseDomain> deleteReviewResponse(RequestParams requestParams) {
        return reputationService.getApi()
                .deleteReviewResponse(AuthUtil.generateParamsNetwork2(MainApplication.getAppContext(),
                        requestParams.getParameters()))
                .map(deleteReviewResponseMapper);
    }
}
