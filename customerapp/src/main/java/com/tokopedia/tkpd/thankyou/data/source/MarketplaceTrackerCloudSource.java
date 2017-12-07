package com.tokopedia.tkpd.thankyou.data.source;

import android.util.Log;
import com.apollographql.android.rx.RxApollo;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloWatcher;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.thankyou.PaymentQuery;
import com.tokopedia.tkpd.thankyou.domain.model.ThanksTrackerConst;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 12/5/17.
 */

public class MarketplaceTrackerCloudSource extends ThanksTrackerCloudSource {
    private ApolloClient apolloClient;

    public MarketplaceTrackerCloudSource(RequestParams requestParams,
                                         ApolloClient apolloClient) {
        super(requestParams);
        this.apolloClient = apolloClient;
    }

    @Override
    public Observable<String> sendAnalytics() {
        ApolloWatcher<PaymentQuery.Data> apolloWatcher = apolloClient.newCall(
                PaymentQuery.builder().paymentId(
                        Integer.parseInt(
                                requestParams.getString(ThanksTrackerConst.Key.ID, "0")
                )).build()
        ).watcher();

        return RxApollo.from(apolloWatcher).map(new Func1<PaymentQuery.Data, String>() {
            @Override
            public String call(PaymentQuery.Data data) {
                Log.d("oka", "paymentId:" + data.payment().payment_id());
                Log.d("oka", "paymentData:" + data.toString());
                return null;
            }
        });
    }
}
