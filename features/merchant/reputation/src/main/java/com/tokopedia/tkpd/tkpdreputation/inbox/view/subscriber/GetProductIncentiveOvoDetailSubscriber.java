package com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber;

import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.ProductRevIncentiveOvoDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;

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
