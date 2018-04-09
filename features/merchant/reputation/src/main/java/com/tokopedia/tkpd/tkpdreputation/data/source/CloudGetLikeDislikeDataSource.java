package com.tokopedia.tkpd.tkpdreputation.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.tkpd.tkpdreputation.domain.model.GetLikeDislikeReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.GetLikeDislikeMapper;

import rx.Observable;

/**
 * @author by nisie on 9/29/17.
 */

public class CloudGetLikeDislikeDataSource {
    private final ReputationService reputationService;
    private final GetLikeDislikeMapper getLikeDislikeMapper;

    public CloudGetLikeDislikeDataSource(ReputationService reputationService,
                                         GetLikeDislikeMapper getLikeDislikeMapper) {

        this.reputationService = reputationService;
        this.getLikeDislikeMapper = getLikeDislikeMapper;
    }


    public Observable<GetLikeDislikeReviewDomain> getLikeDislikeReview(RequestParams requestParams) {
        return reputationService.getApi()
                .getLikeDislikeReview(requestParams.getParameters())
                .map(getLikeDislikeMapper);
    }
}
