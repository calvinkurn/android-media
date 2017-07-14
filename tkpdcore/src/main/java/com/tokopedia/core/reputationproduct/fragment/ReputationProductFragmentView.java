package com.tokopedia.core.reputationproduct.fragment;

import com.tokopedia.core.inboxreputation.model.actresult.ActResult;
import com.tokopedia.core.reputationproduct.model.LikeDislike;
import com.tokopedia.core.review.model.product_review.ReviewProductModel;

/**
 * Created by yoasfs on 13/07/17.
 */

public interface ReputationProductFragmentView {

    void setResultToModel(LikeDislike resultToModel);


    void onSuccessDeleteComment(ActResult result);
    void onSuccessLikeDislikeReview(ActResult result);
    void onSuccessGetLikeDislikeReview();
    void onErrorGetLikeDislikeReview(ReviewProductModel model, String err);
    void onErrorConnectionGetLikeDislikeReview(ReviewProductModel model);
    void onError(String error);
    void onErrorConnection();
}
