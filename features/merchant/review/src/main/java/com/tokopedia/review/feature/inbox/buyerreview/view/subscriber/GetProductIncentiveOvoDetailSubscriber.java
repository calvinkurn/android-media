package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber;

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.ProductRevIncentiveOvoDomain;
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail;

import rx.Subscriber;

public class GetProductIncentiveOvoDetailSubscriber extends Subscriber<ProductRevIncentiveOvoDomain> {

    private InboxReputationDetail.View viewListener;

    public GetProductIncentiveOvoDetailSubscriber(InboxReputationDetail.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorGetProductRevIncentiveOvo(e);
    }

    @Override
    public void onNext(ProductRevIncentiveOvoDomain productRevIncentiveOvoDomain) {
        viewListener.onSuccessGetProductRevIncentiveOvo(productRevIncentiveOvoDomain);
    }
}
