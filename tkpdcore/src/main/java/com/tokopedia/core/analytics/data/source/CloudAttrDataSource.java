package com.tokopedia.core.analytics.data.source;

import com.apollographql.android.rx.RxApollo;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloWatcher;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.anals.UserAttribute;
import com.tokopedia.core.analytics.domain.usecase.GetUserAttributesUseCase;
import com.tokopedia.core.base.domain.RequestParams;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Herdi_WORK on 27.09.17.
 */

public class CloudAttrDataSource {

    private ApolloClient apolloClient;

    public CloudAttrDataSource( ApolloClient aplClient){
        apolloClient = aplClient;
    }

    public Observable<String> getUserAttributes(RequestParams requestParams){

        CommonUtils.dumper("rxapollo called userID "+requestParams.getInt(GetUserAttributesUseCase.PARAM_USER_ID, 0));

        ApolloWatcher<UserAttribute.Data> apolloWatcher = apolloClient.newCall(UserAttribute.builder()
                .userID(requestParams.getInt(GetUserAttributesUseCase.PARAM_USER_ID, 0))
                .build()
                ).watcher();

        return RxApollo.from(apolloWatcher).map(new Func1<UserAttribute.Data, String>() {
            @Override
            public String call(UserAttribute.Data data) {


                CommonUtils.dumper("rxapollo "+data.toString());
                return data.toString();
            }
        });
    }
}
