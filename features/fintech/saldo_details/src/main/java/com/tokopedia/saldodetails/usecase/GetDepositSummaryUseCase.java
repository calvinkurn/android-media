package com.tokopedia.saldodetails.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.CacheType;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.response.model.GqlAllDepositSummaryResponse;
import com.tokopedia.saldodetails.response.model.GqlBuyerDepositSummaryResponse;

import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants.cacheDuration;

public class GetDepositSummaryUseCase {

    private static final String GET_SUMMARY_DEPOSIT = "DepositActivityQuery";
    private GraphqlUseCase graphqlUseCase;
    private Context context;
    private boolean isRequesting;
    private Map<String, Object> variables;
    private boolean isSeller;

    @Inject
    public GetDepositSummaryUseCase(@ApplicationContext Context context) {
        graphqlUseCase = new GraphqlUseCase();
        this.context = context;
    }

    public void unsubscribe() {
        if (graphqlUseCase != null) {
            graphqlUseCase.unsubscribe();
        }
    }

    public void setRequestVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public void execute(Subscriber<GraphqlResponse> subscriber) {
        graphqlUseCase.clearRequest();
        setRequesting(true);

        String query;
        GraphqlRequest graphqlRequest;

        if (isSeller) {
            query = GraphqlHelper.loadRawString(context.getResources(), R.raw.query_deposit_details_for_all);

            graphqlRequest = new GraphqlRequest(
                    query,
                    GqlAllDepositSummaryResponse.class,
                    variables, GET_SUMMARY_DEPOSIT);
        } else {
            query = GraphqlHelper.loadRawString(context.getResources(), R.raw.query_deposit_details_for_buyer);
            graphqlRequest = new GraphqlRequest(
                    query,
                    GqlBuyerDepositSummaryResponse.class,
                    variables, GET_SUMMARY_DEPOSIT);
        }

        GraphqlCacheStrategy cacheStrategy =
                new GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                        .setExpiryTime(cacheDuration).setSessionIncluded(true).build();
        graphqlUseCase.setCacheStrategy(cacheStrategy);
        graphqlUseCase.addRequest(graphqlRequest);
        graphqlUseCase.execute(subscriber);
    }

    public boolean isRequesting() {
        return isRequesting;
    }

    public void setRequesting(boolean isRequesting) {
        this.isRequesting = isRequesting;
    }

    public void setIsSeller(boolean seller) {
        this.isSeller = seller;
    }
}
