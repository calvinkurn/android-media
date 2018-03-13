package com.tokopedia.tkpd.tkpdreputation.data.source;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.LikeDislikeMapper;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain;

import rx.Observable;

/**
 * @author by nisie on 9/29/17.
 */

public class CloudLikeDislikeDataSource {

    private final ReputationService reputationService;
    private final LikeDislikeMapper likeDislikeMapper;

    public CloudLikeDislikeDataSource(ReputationService reputationService,
                                      LikeDislikeMapper likeDislikeMapper) {
        this.reputationService = reputationService;
        this.likeDislikeMapper = likeDislikeMapper;
    }

    public Observable<LikeDislikeDomain> getLikeDislikeReview(RequestParams requestParams) {
        return reputationService.getApi()
                .likeDislikeReview(AuthUtil.generateParamsNetwork2(MainApplication.getAppContext
                        (),requestParams
                        .getParameters
                        ()))
                .map(likeDislikeMapper);
    }
}
