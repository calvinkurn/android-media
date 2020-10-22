package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber;

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.ProductRevIncentiveOvoDomain;
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputation;

import rx.Subscriber;

public class GetProductIncentiveOvoSubscriber extends Subscriber<ProductRevIncentiveOvoDomain> {

    private InboxReputation.View viewListener;

    public GetProductIncentiveOvoSubscriber(InboxReputation.View viewListener) {
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
