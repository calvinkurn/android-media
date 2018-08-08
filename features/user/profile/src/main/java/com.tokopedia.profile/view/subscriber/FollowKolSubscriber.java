package com.tokopedia.profile.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.kol.feature.post.data.pojo.FollowKolQuery;
import com.tokopedia.profile.view.listener.TopProfileActivityListener;

import rx.Subscriber;

/**
 * @author by milhamj on 01/03/18.
 */

public class FollowKolSubscriber extends Subscriber<GraphqlResponse> {

    private final TopProfileActivityListener.View view;

    public FollowKolSubscriber(TopProfileActivityListener.View view){
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        if (view != null) {
            view.onErrorFollowKol(
                    ErrorHandler.getErrorMessage(view.getContext(), throwable)
            );
        }
    }

    @Override
    public void onNext(GraphqlResponse response) {
        FollowKolQuery query = response.getData(FollowKolQuery.class);
        Boolean isSuccess = query.getData().getData().getStatus() == 1;
        if (view != null) {
            if (isSuccess) {
                view.onSuccessFollowKol();
            } else {
                view.onErrorFollowKol(null);
            }
        }
    }
}
