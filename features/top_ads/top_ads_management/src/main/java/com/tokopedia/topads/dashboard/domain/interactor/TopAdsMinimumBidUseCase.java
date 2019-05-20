package com.tokopedia.topads.dashboard.domain.interactor;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.data.model.request.MinimumBidRequest;
import com.tokopedia.topads.dashboard.domain.model.MinimumBidDomain;
import com.tokopedia.topads.dashboard.utils.Utils;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * Author errysuprayogi on 25,March,2019
 */
public class TopAdsMinimumBidUseCase extends UseCase<MinimumBidDomain.TopadsBidInfo> {

    public static final String REQUEST_TYPE = "requestType";
    public static final String SHOP_ID = "shopId";
    public static final String SOURCE = "source";
    private final GraphqlUseCase graphqlUseCase;
    private final Context context;

    public TopAdsMinimumBidUseCase(GraphqlUseCase graphqlUseCase, Context context) {
        this.graphqlUseCase = graphqlUseCase;
        this.context = context;
    }

    @Override
    public Observable<MinimumBidDomain.TopadsBidInfo> createObservable(RequestParams requestParams) {
        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.query_minimum_bid), MinimumBidDomain.class, requestParams.getParameters(), false);
        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map(new Func1<GraphqlResponse, MinimumBidDomain.TopadsBidInfo>() {
                    @Override
                    public MinimumBidDomain.TopadsBidInfo call(GraphqlResponse graphqlResponse) {
                        MinimumBidDomain data = graphqlResponse
                                .getData(MinimumBidDomain.class);
                        return data.getTopadsBidInfo();
                    }
                });
    }

    public RequestParams getBidParams(MinimumBidRequest dataParams) {
        RequestParams params = RequestParams.create();
        try {
            params.putAll(Utils.jsonToMap(new Gson().toJson(dataParams)));
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return params;
        }
    }

}
