package com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.subscriber;

import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.LikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.listener.ReputationProductFragmentView;

import rx.Subscriber;

/**
 * Created by yoasfs on 18/07/17.
 */

public class GetLikeDislikeSubscriber extends Subscriber<LikeDislikeDomain> {

    private final ReputationProductFragmentView reputationProductFragmentView;

    public GetLikeDislikeSubscriber(ReputationProductFragmentView reputationProductFragmentView) {
        this.reputationProductFragmentView = reputationProductFragmentView;
    }
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(LikeDislikeDomain likeDislikeDomain) {
        reputationProductFragmentView.setResultToModel(likeDislikeDomain);
    }
}
