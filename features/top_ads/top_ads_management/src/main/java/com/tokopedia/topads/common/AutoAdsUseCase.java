package com.tokopedia.topads.common;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.topads.common.data.response.TopAdsAutoAdsData;
import com.tokopedia.topads.common.data.response.TopAdsAutoAds;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Author errysuprayogi on 12,June,2019
 */
public class AutoAdsUseCase extends UseCase<TopAdsAutoAdsData> {

    public static final String SHOP_ID = "shopId";
    private Context context;
    private GraphqlUseCase graphqlUseCase;
    private UserSessionInterface session;

    @Inject
    public AutoAdsUseCase(@ApplicationContext Context context, GraphqlUseCase graphqlUseCase,
                          UserSessionInterface session) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
        this.session = session;
    }

    @Override
    public Observable<TopAdsAutoAdsData> createObservable(RequestParams requestParams) {
        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(getRequest());
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map(new Func1<GraphqlResponse, TopAdsAutoAdsData>() {
            @Override
            public TopAdsAutoAdsData call(GraphqlResponse graphqlResponse) {
                TopAdsAutoAds.Response response = graphqlResponse.getData(TopAdsAutoAds.Response.class);
                return response.getAutoAds().getData();
            }
        });
    }

    private GraphqlRequest getRequest() {
        RequestParams params = RequestParams.create();
        params.putInt(SHOP_ID, Integer.parseInt(session.getShopId()));
        return new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.query_auto_ads_status),
                TopAdsAutoAds.Response.class, params.getParameters()
        );
    }
}
