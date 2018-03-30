package com.tokopedia.core.analytics.data.source;

import com.apollographql.android.rx.RxApollo;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloWatcher;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.anals.ConsumerDrawerData;
import com.tokopedia.anals.SellerDrawerData;
import com.tokopedia.core.analytics.domain.usecase.GetUserAttributesUseCase;
import com.tokopedia.core.analytics.handler.AnalyticsCacheHandler;
import com.tokopedia.core.base.domain.RequestParams;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Herdi_WORK on 27.09.17.
 */

public class CloudAttrDataSource {

    private ApolloClient apolloClient;
    private AnalyticsCacheHandler analyticsCacheHandler;

    public CloudAttrDataSource(ApolloClient aplClient) {
        apolloClient = aplClient;
        analyticsCacheHandler = new AnalyticsCacheHandler();
    }

    public Observable<ConsumerDrawerData.Data> getConsumerUserAttributes(RequestParams requestParams) {

        CommonUtils.dumper("rxapollo called userID " + requestParams.getInt(GetUserAttributesUseCase.PARAM_USER_ID, 0));

        ApolloWatcher<ConsumerDrawerData.Data> apolloWatcher = apolloClient.newCall(ConsumerDrawerData.builder()
                .userID(requestParams.getInt(GetUserAttributesUseCase.PARAM_USER_ID, 0))
                .build()
        ).watcher();

        return RxApollo.from(apolloWatcher).doOnNext(setToCache());
    }

    public Observable<SellerDrawerData.Data> getSellerUserAttributes(RequestParams requestParams) {

        CommonUtils.dumper("rxapollo called userID " + requestParams.getInt(GetUserAttributesUseCase.PARAM_USER_ID, 0));

        ApolloWatcher<SellerDrawerData.Data> apolloWatcher = apolloClient.newCall(SellerDrawerData.builder()
                .userID(requestParams.getInt(GetUserAttributesUseCase.PARAM_USER_ID, 0))
                .build()
        ).watcher();

        return RxApollo.from(apolloWatcher).map(new Func1<SellerDrawerData.Data, SellerDrawerData.Data>() {
            @Override
            public SellerDrawerData.Data call(SellerDrawerData.Data data) {
                return data;
            }
        });//.doOnNext(setToCache());
    }

    private Action1<ConsumerDrawerData.Data> setToCache() {
        return new Action1<ConsumerDrawerData.Data>() {
            @Override
            public void call(ConsumerDrawerData.Data data) {
                if (data != null)
                    analyticsCacheHandler.setUserDataGraphQLCache(data);
            }
        };
    }
}
