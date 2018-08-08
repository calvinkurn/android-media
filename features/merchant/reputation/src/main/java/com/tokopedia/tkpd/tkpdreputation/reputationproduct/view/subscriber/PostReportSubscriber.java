package com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.subscriber;

import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.ActResultDomain;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.listener.ReputationProductFragmentView;

import rx.Subscriber;

/**
 * Created by yoasfs on 18/07/17.
 */

public class PostReportSubscriber extends Subscriber<ActResultDomain> {

    private final ReputationProductFragmentView reputationProductFragmentView;

    public PostReportSubscriber(ReputationProductFragmentView reputationProductFragmentView) {
        this.reputationProductFragmentView = reputationProductFragmentView;
    }
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        reputationProductFragmentView.onError(e.getLocalizedMessage());
    }

    @Override
    public void onNext(ActResultDomain actResultDomain) {
        reputationProductFragmentView.onSuccessReportReview(actResultDomain);
    }
}
