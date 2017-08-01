package com.tokopedia.core.reputationproduct.view.subscriber;

import com.tokopedia.core.reputationproduct.domain.model.ActResultDomain;
import com.tokopedia.core.reputationproduct.domain.model.LikeDislikeDomain;
import com.tokopedia.core.reputationproduct.fragment.ReputationProductFragmentView;
import com.tokopedia.core.review.model.product_review.ReviewProductModel;

import rx.Subscriber;

/**
 * Created by yoasfs on 18/07/17.
 */

public class ActionLikeDislikeSubscriber extends Subscriber<ActResultDomain> {

    private final ReputationProductFragmentView reputationProductFragmentView;
    private final ReviewProductModel reviewProductModel;

    public ActionLikeDislikeSubscriber(ReputationProductFragmentView reputationProductFragmentView, ReviewProductModel reviewProductModel) {
        this.reputationProductFragmentView = reputationProductFragmentView;
        this.reviewProductModel = reviewProductModel;
    }
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        reputationProductFragmentView.onErrorGetLikeDislikeReview(reviewProductModel, e.getLocalizedMessage());
    }

    @Override
    public void onNext(ActResultDomain actResultDomain) {
        reputationProductFragmentView.onSuccessLikeDislikeReview(actResultDomain);
    }
}
