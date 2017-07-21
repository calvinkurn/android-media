package com.tokopedia.core.reputationproduct.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.product.ReviewActService;
import com.tokopedia.core.reputationproduct.data.mapper.ActResultMapper;
import com.tokopedia.core.reputationproduct.domain.model.ActResultDomain;

import java.util.Map;

import rx.Observable;

/**
 * Created by yoasfs on 18/07/17.
 */

public class CloudDeleteCommentDataSource {

    private Context context;
    private ReviewActService reviewActService;
    private ActResultMapper actResultMapper;

    public CloudDeleteCommentDataSource(Context context,
                                        ReviewActService reviewActService,
                                        ActResultMapper actResultMapper) {
        this.context = context;
        this.reviewActService = reviewActService;
        this.actResultMapper = actResultMapper;
    }

    public Observable<ActResultDomain> getDeleteCommentDataSource(Map<String, String> parameters) {
        return reviewActService.getApi().deleteCommentReview(parameters)
                .map(actResultMapper);
    }
}
