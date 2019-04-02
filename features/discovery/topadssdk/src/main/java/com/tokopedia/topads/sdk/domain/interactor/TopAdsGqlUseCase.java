package com.tokopedia.topads.sdk.domain.interactor;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.domain.model.TopAdsGqlResponse;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Author errysuprayogi on 29,November,2018
 */
public class TopAdsGqlUseCase extends UseCase<TopAdsModel> {

    private Context context;
    private GraphqlUseCase graphqlUseCase;

    public TopAdsGqlUseCase(Context context) {
        this.context = context;
        graphqlUseCase = new GraphqlUseCase();
    }

    @Override
    public Observable<TopAdsModel> createObservable(RequestParams requestParams) {
        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.get_topads_query),
                TopAdsGqlResponse.class,
                requestParams.getParameters(), false);
        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        return graphqlUseCase.getExecuteObservable(requestParams).map(new Func1<GraphqlResponse, TopAdsModel>() {
            @Override
            public TopAdsModel call(GraphqlResponse graphqlResponse) {
                TopAdsGqlResponse response = graphqlResponse.getData(TopAdsGqlResponse.class);
                return response.getAdsModel();
            }
        });
    }

}
