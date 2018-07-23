package com.tokopedia.explore.view.subscriber;

import com.tokopedia.explore.domain.entity.GetExploreData;
import com.tokopedia.explore.view.listener.ContentExploreContract;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import rx.Subscriber;

/**
 * @author by milhamj on 23/07/18.
 */

public class GetExploreDataSubscriber extends Subscriber<GraphqlResponse> {

    private final ContentExploreContract.View listener;

    public GetExploreDataSubscriber(ContentExploreContract.View listener) {
        this.listener = listener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        GetExploreData data = graphqlResponse.getData(GetExploreData.class);
    }
}
