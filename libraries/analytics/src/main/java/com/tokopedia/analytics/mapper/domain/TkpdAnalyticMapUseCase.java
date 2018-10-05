package com.tokopedia.analytics.mapper.domain;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.analytics.R;
import com.tokopedia.analytics.mapper.TkpdAppsFlyerRouter;
import com.tokopedia.analytics.mapper.model.AnalyticsMappingRequest;
import com.tokopedia.analytics.mapper.model.AnalyticsMappingResponse;
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

public class TkpdAnalyticMapUseCase extends UseCase<Boolean> {
    private final CacheManager cacheManager;
    Context context;

    public static final String USER_ID = "customerUserId";
    public static final String APPSFLYER_ID = "appsflyerId";


    @Inject
    public TkpdAnalyticMapUseCase(@ApplicationContext Context context, CacheManager cacheManager) {
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
        if(userID.equals(cacheManager.get(USER_ID)) && appsFlyerId.equals(cacheManager.get(APPSFLYER_ID))) {
            return Observable.just(true);
        }
        AnalyticsMappingRequest analyticsMappingRequest = new AnalyticsMappingRequest();
        analyticsMappingRequest.setAppsflyerId(appsFlyerId);
        analyticsMappingRequest.setCustomerUserId(userID);
        requestParams.putObject("input",analyticsMappingRequest);
        GraphqlClient.init(context);
        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.gql_appsflyer_mapping), AnalyticsMappingResponse.class, requestParams.getParameters());

        String finalUserID = userID;
        String finalAppsFlyerId = appsFlyerId;
        return ObservableFactory.create(Arrays.asList(graphqlRequest), null).map(new Func1<GraphqlResponse, Boolean>() {
            @Override
            public Boolean call(GraphqlResponse graphqlResponse) {
                AnalyticsMappingResponse response = graphqlResponse.getData(AnalyticsMappingResponse.class);
                cacheManager.save(USER_ID, finalUserID,30*24*60*60);
                cacheManager.save(APPSFLYER_ID, finalAppsFlyerId,30*24*60*60);
                return response.getAppsflyerMapping().getMsg().equalsIgnoreCase("success")?true:false;
            }
        });
    }
}
