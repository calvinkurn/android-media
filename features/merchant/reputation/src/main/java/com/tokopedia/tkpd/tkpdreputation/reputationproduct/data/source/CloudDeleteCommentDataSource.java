package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.source;

import android.content.Context;

import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.tkpd.tkpdreputation.network.shop.ReputationActService;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.mapper.ActResultMapper;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.ActResultDomain;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.Map;

import rx.Observable;

/**
 * Created by yoasfs on 18/07/17.
 */

public class CloudDeleteCommentDataSource {

    private ReputationActService reputationActService;
    private ActResultMapper actResultMapper;
    private UserSessionInterface userSessionInterface;

    public CloudDeleteCommentDataSource(ReputationActService reputationActService,
                                        ActResultMapper actResultMapper,
                                        UserSessionInterface userSessionInterface) {
        this.reputationActService = reputationActService;
        this.actResultMapper = actResultMapper;
        this.userSessionInterface = userSessionInterface;
    }

    public Observable<ActResultDomain> getDeleteCommentDataSource(Map<String, String> parameters) {
        return reputationActService.getApi().deleteRepReviewResponse(AuthHelper.generateParamsNetwork(
                userSessionInterface.getUserId(),
                userSessionInterface.getDeviceId(),
                parameters
        )).map(actResultMapper);
    }
}
