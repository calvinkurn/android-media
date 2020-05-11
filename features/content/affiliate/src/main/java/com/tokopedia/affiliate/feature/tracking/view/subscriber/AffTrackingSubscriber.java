package com.tokopedia.affiliate.feature.tracking.view.subscriber;

import com.tokopedia.config.GlobalConfig;
import com.tokopedia.affiliate.feature.tracking.domain.model.Data;
import com.tokopedia.affiliate.feature.tracking.view.contract.AffContract;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import rx.Subscriber;

/**
 * @author by milhamj on 22/10/18.
 */

public class AffTrackingSubscriber extends Subscriber<GraphqlResponse> {

    private final AffContract.View view;

    public AffTrackingSubscriber(AffContract.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {
        view.finishActivity();
    }

    @Override
    public void onError(Throwable e) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace();
        }
        view.handleError();
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        if (graphqlResponse != null && graphqlResponse.getData(Data.class) != null) {
            Data data = graphqlResponse.getData(Data.class);
            if (data != null) {
                view.handleLink(data.getBymeGetRealURL().getRealURL());
            } else {
                view.handleError();
            }
        } else {
            view.handleError();
        }
    }
}
