package com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.usecase;

import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.ActReviewPass;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.repository.LikeDislikeRepository;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.ActResultDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by yoasfs on 18/07/17.
 */

public class LikeDislikeUseCase extends UseCase<ActResultDomain> {
    public static final String PARAM_REVIEW_ID = "review_ids";
    public static final String PARAM_SHOP_ID = "shop_id";
    protected LikeDislikeRepository likeDislikeRepository;

    public LikeDislikeUseCase(LikeDislikeRepository likeDislikeRepository) {
        super();
        this.likeDislikeRepository = likeDislikeRepository;
    }

    @Override
    public Observable<ActResultDomain> createObservable(RequestParams requestParams) {
        return likeDislikeRepository.getLikeDislikeRepository(requestParams.getParamsAllValueInString());
    }

    public RequestParams getActionLikeDislikeParam(String reviewId, String productID, String shopID, String statusLikeDislike) {
        ActReviewPass pass = new ActReviewPass();
        RequestParams requestParams = RequestParams.create();
        pass.setReviewId(String.valueOf(reviewId));
        pass.setProductId(productID);
        pass.setShopId(shopID);
        pass.setLikeStatus(statusLikeDislike);
        requestParams = pass.getLikeDislikeRequestParam();
        return requestParams;
    }
}