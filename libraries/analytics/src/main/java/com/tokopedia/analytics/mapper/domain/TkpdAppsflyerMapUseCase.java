package com.tokopedia.analytics.mapper.domain;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.analytics.R;
import com.tokopedia.analytics.mapper.TkpdAppsFlyerRouter;
import com.tokopedia.analytics.mapper.model.AppsflyerMappingRequest;
import com.tokopedia.analytics.mapper.model.AppsflyerMappingResponse;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.data.ObservableFactory;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.Arrays;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class TkpdAppsflyerMapUseCase extends UseCase<Boolean> {
    private final CacheManager cacheManager;
    Context context;

    public static final String USER_ID = "customerUserId";
    public static final String APPSFLYER_ID = "appsflyerId";


    @Inject
    public TkpdAppsflyerMapUseCase(@ApplicationContext Context context, CacheManager cacheManager) {
        this.context = context;
        this.cacheManager = cacheManager;
    }


    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {

        String userID = "";
        String appsFlyerId = "";
        if (context.getApplicationContext() instanceof TkpdAppsFlyerRouter) {
            appsFlyerId = ((TkpdAppsFlyerRouter) context.getApplicationContext()).getAppsFlyerID();
            userID = ((TkpdAppsFlyerRouter) context.getApplicationContext()).getUserId();
        }
        //check if userID Same not to go further
        if(userID == null || userID.isEmpty() || appsFlyerId == null || appsFlyerId.isEmpty()) {
            return Observable.just(true);
        }
        if(userID.equals(cacheManager.get(USER_ID)) && appsFlyerId.equals(cacheManager.get(APPSFLYER_ID))) {
            return Observable.just(true);
        }
        AppsflyerMappingRequest appsflyerMappingRequest = new AppsflyerMappingRequest();
        appsflyerMappingRequest.setAppsflyerId(appsFlyerId);
        appsflyerMappingRequest.setCustomerUserId(userID);
        requestParams.putObject("input", appsflyerMappingRequest);
        GraphqlClient.init(context);
        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.gql_appsflyer_mapping), AppsflyerMappingResponse.class, requestParams.getParameters(), false);

        String finalUserID = userID;
        String finalAppsFlyerId = appsFlyerId;
        return ObservableFactory.create(Arrays.asList(graphqlRequest), null).map(new Func1<GraphqlResponse, Boolean>() {
            @Override
            public Boolean call(GraphqlResponse graphqlResponse) {
                AppsflyerMappingResponse response = graphqlResponse.getData(AppsflyerMappingResponse.class);
                cacheManager.save(USER_ID, finalUserID,30*24*60*60);
                cacheManager.save(APPSFLYER_ID, finalAppsFlyerId,30*24*60*60);
                return response.getAppsflyerMapping().getMsg().equalsIgnoreCase("success")?true:false;
            }
        });
    }
}
