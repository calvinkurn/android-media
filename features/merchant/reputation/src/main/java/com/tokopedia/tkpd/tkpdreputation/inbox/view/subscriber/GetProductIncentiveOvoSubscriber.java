package com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber;

import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.ProductRevIncentiveOvoDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputation;

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
