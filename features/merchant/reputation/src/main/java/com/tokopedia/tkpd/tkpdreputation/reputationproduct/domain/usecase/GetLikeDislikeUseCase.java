package com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.repository.GetLikeDislikeRepository;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.LikeDislikeDomain;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by yoasfs on 18/07/17.
 */

public class GetLikeDislikeUseCase extends UseCase<LikeDislikeDomain> {
    public static final String PARAM_REVIEW_ID = "review_ids";
    public static final String PARAM_SHOP_ID = "shop_id";
    protected GetLikeDislikeRepository getLikeDislikeRepository;

    public GetLikeDislikeUseCase(ThreadExecutor threadExecutor,
                           PostExecutionThread postExecutionThread,
                           GetLikeDislikeRepository getLikeDislikeRepository) {
        super(threadExecutor, postExecutionThread);
        this.getLikeDislikeRepository = getLikeDislikeRepository;
    }

    @Override
    public Observable<LikeDislikeDomain> createObservable(RequestParams requestParams) {
        return getLikeDislikeRepository.getGetLikeDislikeRepository(requestParams.getParamsAllValueInString());
    }

    public Map<String, String> getLikeDislikeParam(String shopId, String reviewId) {
        HashMap<String, String> param = new HashMap<>();
        param.put(PARAM_SHOP_ID, shopId);
        param.put(PARAM_REVIEW_ID, String.valueOf(reviewId));
        return param;
    }

    public RequestParams getLikeDislikeRequestParams(String shopId, String reviewId) {
        RequestParams params  = RequestParams.create();
        params.putString(PARAM_SHOP_ID, shopId);
        params.putString(PARAM_REVIEW_ID, String.valueOf(reviewId));
        return params;
    }
}