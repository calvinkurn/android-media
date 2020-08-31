package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.source;

import android.content.Context;

import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.tkpd.tkpdreputation.network.product.ReviewActService;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.mapper.ActResultMapper;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.ActResultDomain;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.Map;

import rx.Observable;

/**
 * Created by yoasfs on 18/07/17.
 */

public class CloudPostReportDataSource {

    private ReviewActService reviewActService;
    private ActResultMapper actResultMapper;
    private UserSessionInterface userSessionInterface;

    public CloudPostReportDataSource(ReviewActService reviewActService,
                                     ActResultMapper actResultMapper,
                                     UserSessionInterface userSessionInterface) {
        this.reviewActService = reviewActService;
        this.actResultMapper = actResultMapper;
        this.userSessionInterface = userSessionInterface;
    }

    public Observable<ActResultDomain> getPostReportDataSource(Map<String, String> parameters) {
        return reviewActService.getApi().reportReview(AuthHelper.generateParamsNetwork(
                userSessionInterface.getUserId(),
                userSessionInterface.getDeviceId(),
                parameters
        )).map(actResultMapper);
    }
}
