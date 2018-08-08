package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SkipReviewMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SkipReviewDomain;

import rx.Observable;

/**
 * @author by nisie on 9/12/17.
 */

public class CloudSkipReviewDataSource {

    private final ReputationService reputationService;
    private final SkipReviewMapper skipReviewMapper;

    public CloudSkipReviewDataSource(ReputationService reputationService,
                                     SkipReviewMapper skipReviewMapper) {
        this.reputationService = reputationService;
        this.skipReviewMapper = skipReviewMapper;
    }


    public Observable<SkipReviewDomain> skipReview(RequestParams requestParams) {
        return reputationService.getApi()
                .skipReview(AuthUtil.generateParamsNetwork2(MainApplication.getAppContext(),
                        requestParams.getParameters()))
                .map(skipReviewMapper);
    }
}
