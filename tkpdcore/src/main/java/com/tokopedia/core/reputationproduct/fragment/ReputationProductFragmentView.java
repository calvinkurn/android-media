package com.tokopedia.core.reputationproduct.fragment;

import com.tokopedia.core.reputationproduct.domain.model.ActResultDomain;
import com.tokopedia.core.reputationproduct.domain.model.LikeDislikeDomain;
import com.tokopedia.core.review.model.product_review.ReviewProductModel;

/**
 * Created by yoasfs on 13/07/17.
 */

public interface ReputationProductFragmentView {

    void setResultToModel(LikeDislikeDomain resultToModel);

    void onSuccessDeleteComment(ActResultDomain result);

    void onSuccessLikeDislikeReview(ActResultDomain result);

    void onSuccessGetLikeDislikeReview();

    void onErrorGetLikeDislikeReview(ReviewProductModel model, String err);

    void onErrorConnectionGetLikeDislikeReview(ReviewProductModel model);

    void onError(String error);

    void onErrorConnection();
}
