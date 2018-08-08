package com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.listener;

import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.ActResultDomain;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.LikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.product_review.ReviewProductModel;

/**
 * Created by yoasfs on 13/07/17.
 */

public interface ReputationProductFragmentView {

    void setResultToModel(LikeDislikeDomain resultToModel);

    void onSuccessDeleteComment(ActResultDomain result);

    void onSuccessLikeDislikeReview(ActResultDomain result);

    void onSuccessReportReview(ActResultDomain resultDomain);

    void onSuccessGetLikeDislikeReview();

    void onErrorGetLikeDislikeReview(ReviewProductModel model, String err);

    void onErrorConnectionGetLikeDislikeReview(ReviewProductModel model);

    void onError(String error);

    void onErrorConnection();
}
