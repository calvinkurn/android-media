package com.tokopedia.topads.keyword.domain.interactor;

import android.content.res.Resources;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.data.util.Utils;
import com.tokopedia.topads.dashboard.data.model.request.MinimumBidRequest;
import com.tokopedia.topads.dashboard.domain.model.MinimumBidDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class TopAdsKeyMinimumBidUseCase extends UseCase<MinimumBidDomain.TopadsBidInfo> {
    private final GraphqlUseCase graphqlUseCase;
    private String query;

@Inject
    public TopAdsKeyMinimumBidUseCase(GraphqlUseCase graphqlUseCase) {
        this.graphqlUseCase = graphqlUseCase;
    }

    @Override
    public Observable<MinimumBidDomain.TopadsBidInfo> createObservable(RequestParams requestParams) {
        GraphqlRequest graphqlRequest = new GraphqlRequest(query, MinimumBidDomain.class, requestParams.getParameters(), false);
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

    public void setQuery(@NotNull Resources resources) {
        this.query = GraphqlHelper.loadRawString(resources,
                R.raw.query_minimum_bid);
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
