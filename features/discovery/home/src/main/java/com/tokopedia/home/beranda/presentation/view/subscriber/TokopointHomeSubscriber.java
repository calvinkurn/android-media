package com.tokopedia.home.beranda.presentation.view.subscriber;

import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.home.beranda.data.model.TokopointHomeDrawerData;
import com.tokopedia.home.beranda.data.model.TokopointsDrawerHomeData;
import com.tokopedia.home.beranda.presentation.view.HomeContract;

import rx.Subscriber;

/**
 * Created by meta on 16/07/18.
 */
public class TokopointHomeSubscriber extends Subscriber<GraphqlResponse> {

    private HomeContract.Presenter presenter;

    public TokopointHomeSubscriber(HomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onCompleted() { }

    @Override
    public void onError(Throwable e) {
        presenter.onHeaderTokopointError();
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        TokopointsDrawerHomeData tokopointsDrawerHomeData=graphqlResponse.getData(TokopointsDrawerHomeData.class);
        presenter.updateHeaderTokoPointData(tokopointsDrawerHomeData);
    }
}
