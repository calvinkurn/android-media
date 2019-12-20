package com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.usecase;

import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.repository.GetLikeDislikeRepository;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.LikeDislikeDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by yoasfs on 18/07/17.
 */

public class GetLikeDislikeUseCase extends UseCase<LikeDislikeDomain> {
    public static final String PARAM_REVIEW_ID = "review_ids";
    public static final String PARAM_SHOP_ID = "shop_id";
    protected GetLikeDislikeRepository getLikeDislikeRepository;

    public GetLikeDislikeUseCase(GetLikeDislikeRepository getLikeDislikeRepository) {
        super();
        this.getLikeDislikeRepository = getLikeDislikeRepository;
    }

    @Override
    public Observable<LikeDislikeDomain> createObservable(RequestParams requestParams) {
        return getLikeDislikeRepository.getGetLikeDislikeRepository(requestParams.getParamsAllValueInString());
    }

    public RequestParams getLikeDislikeRequestParams(String shopId, String reviewId) {
        RequestParams params  = RequestParams.create();
        params.putString(PARAM_SHOP_ID, shopId);
        params.putString(PARAM_REVIEW_ID, String.valueOf(reviewId));
        return params;
    }
}