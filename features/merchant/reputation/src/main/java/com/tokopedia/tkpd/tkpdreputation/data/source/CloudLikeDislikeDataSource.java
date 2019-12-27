package com.tokopedia.tkpd.tkpdreputation.data.source;

import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.LikeDislikeMapper;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.network.ReputationService;
import com.tokopedia.tkpd.tkpdreputation.utils.ReputationUtil;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import rx.Observable;

/**
 * @author by nisie on 9/29/17.
 */

public class CloudLikeDislikeDataSource {

    private final ReputationService reputationService;
    private final LikeDislikeMapper likeDislikeMapper;
    private UserSessionInterface userSession;

    public CloudLikeDislikeDataSource(ReputationService reputationService,
                                      LikeDislikeMapper likeDislikeMapper,
                                      UserSessionInterface userSession) {
        this.reputationService = reputationService;
        this.likeDislikeMapper = likeDislikeMapper;
        this.userSession = userSession;
    }

    public Observable<LikeDislikeDomain> getLikeDislikeReview(RequestParams requestParams) {
        return reputationService.getApi()
                .likeDislikeReview(AuthHelper.generateParamsNetwork(
                        userSession.getUserId(),
                        userSession.getDeviceId(),
                        ReputationUtil.convertMapObjectToString(requestParams.getParameters())
                ))
                .map(likeDislikeMapper);
    }
}
