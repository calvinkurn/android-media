package com.tokopedia.saldodetails.subscriber;

import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.saldodetails.deposit.listener.MerchantSaldoDetailsActionListener;
import com.tokopedia.saldodetails.response.model.GqlMerchantSaldoDetailsResponse;

import rx.Subscriber;

public class GetMerchantSaldoDetailsSubscriber extends Subscriber<GraphqlResponse> {

    private MerchantSaldoDetailsActionListener viewListener;

    public GetMerchantSaldoDetailsSubscriber(MerchantSaldoDetailsActionListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.hideSaldoPrioritasFragment();
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {

        if (graphqlResponse != null &&
                graphqlResponse.getData(GqlMerchantSaldoDetailsResponse.class) != null) {

            GqlMerchantSaldoDetailsResponse gqlMerchantSaldoDetailsResponse =
                    graphqlResponse.getData(GqlMerchantSaldoDetailsResponse.class);

            viewListener.showSaldoPrioritasFragment(gqlMerchantSaldoDetailsResponse.getData());

        } else {
            viewListener.hideSaldoPrioritasFragment();
        }
    }
}
