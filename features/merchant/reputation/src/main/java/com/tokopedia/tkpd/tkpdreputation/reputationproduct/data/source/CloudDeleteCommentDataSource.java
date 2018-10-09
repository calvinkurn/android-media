package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.shop.ReputationActService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.mapper.ActResultMapper;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.ActResultDomain;

import java.util.Map;

import rx.Observable;

/**
 * Created by yoasfs on 18/07/17.
 */

public class CloudDeleteCommentDataSource {

    private Context context;
    private ReputationActService reputationActService;
    private ActResultMapper actResultMapper;

    public CloudDeleteCommentDataSource(Context context,
                                        ReputationActService reputationActService,
                                        ActResultMapper actResultMapper) {
        this.context = context;
        this.reputationActService = reputationActService;
        this.actResultMapper = actResultMapper;
    }

    public Observable<ActResultDomain> getDeleteCommentDataSource(Map<String, String> parameters) {
        return reputationActService.getApi().deleteRepReviewResponse(AuthUtil.generateParams(context, parameters))
                .map(actResultMapper);
    }
}
