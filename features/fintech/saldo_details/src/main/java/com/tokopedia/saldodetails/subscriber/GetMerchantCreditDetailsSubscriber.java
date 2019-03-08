package com.tokopedia.saldodetails.subscriber;

import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.saldodetails.deposit.listener.MerchantCreditLineActionListener;
import com.tokopedia.saldodetails.response.model.GqlMerchantCreditDetailsResponse;

import rx.Subscriber;

public class GetMerchantCreditDetailsSubscriber extends Subscriber<GraphqlResponse> {

    private MerchantCreditLineActionListener viewListener;

    public GetMerchantCreditDetailsSubscriber(MerchantCreditLineActionListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.hideMerchantCreditLineFragment();
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {

        if (graphqlResponse != null &&
                graphqlResponse.getData(GqlMerchantCreditDetailsResponse.class) != null) {

            GqlMerchantCreditDetailsResponse response =
                    graphqlResponse.getData(GqlMerchantCreditDetailsResponse.class);
            viewListener.showMerchantCreditLineFragment(response.getData());

        } else {
            viewListener.hideMerchantCreditLineFragment();
        }
    }
}
