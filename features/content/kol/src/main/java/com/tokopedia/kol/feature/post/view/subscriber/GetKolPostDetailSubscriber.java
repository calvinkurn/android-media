package com.tokopedia.kol.feature.post.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.kol.feature.post.view.listener.KolPostDetailContract;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostDetailViewModel;

import rx.Subscriber;

/**
 * @author by milhamj on 27/07/18.
 */

public class GetKolPostDetailSubscriber extends Subscriber<GraphqlResponse> {

    private final KolPostDetailContract.View view;

    public GetKolPostDetailSubscriber(KolPostDetailContract.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.onErrorGetKolPotDetail(ErrorHandler.getErrorMessage(view.getContext(), e));
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        view.onSuccessGetKolPostDetail(convertToViewModel());
    }

    private KolPostDetailViewModel convertToViewModel() {
        return null;
    }
}
