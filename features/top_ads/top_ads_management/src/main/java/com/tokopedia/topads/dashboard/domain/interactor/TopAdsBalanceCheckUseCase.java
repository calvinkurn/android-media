package com.tokopedia.topads.dashboard.domain.interactor;

import android.content.res.Resources;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.data.model.response.TopAdsDepositResponse;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.topads.common.data.internal.ParamObject.CREDIT_DATA;
import static com.tokopedia.topads.common.data.internal.ParamObject.SHOP_DATA;
import static com.tokopedia.topads.common.data.internal.ParamObject.SHOP_Id;

public class TopAdsBalanceCheckUseCase extends UseCase<TopAdsDepositResponse.Data> {
    private final GraphqlUseCase graphqlUseCase;
    private String query;

@Inject
    public TopAdsBalanceCheckUseCase(GraphqlUseCase graphqlUseCase) {
        this.graphqlUseCase = graphqlUseCase;
    }

    @Override
    public Observable<TopAdsDepositResponse.Data> createObservable(RequestParams requestParams) {
        GraphqlRequest graphqlRequest = new GraphqlRequest(query, TopAdsDepositResponse.Data.class, requestParams.getParameters(), false);
        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map(new Func1<GraphqlResponse, TopAdsDepositResponse.Data>() {
                    @Override
                    public TopAdsDepositResponse.Data call(GraphqlResponse graphqlResponse) {
                        return graphqlResponse
                                .getData(TopAdsDepositResponse.Data.class);
                    }
                });
    }

    public void setQuery(@NotNull Resources resources) {
        this.query = GraphqlHelper.loadRawString(resources,
                R.raw.query_topads_deposit);
    }

    public static RequestParams createRequestParams(int shopId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(SHOP_Id, shopId);
        requestParams.putString(CREDIT_DATA, "unclaimed");
        requestParams.putString(SHOP_DATA, "0");
        return requestParams;
    }

}
