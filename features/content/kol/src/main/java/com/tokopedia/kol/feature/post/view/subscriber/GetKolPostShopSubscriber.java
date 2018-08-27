package com.tokopedia.kol.feature.post.view.subscriber;

import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.kol.feature.post.view.listener.KolPostShopContract;

import rx.Subscriber;

/**
 * @author by milhamj on 23/08/18.
 */

public class GetKolPostShopSubscriber extends Subscriber<GraphqlResponse> {

    private final KolPostShopContract.View view;


    public GetKolPostShopSubscriber(KolPostShopContract.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.hideLoading();
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        view.hideLoading();
    }
}
