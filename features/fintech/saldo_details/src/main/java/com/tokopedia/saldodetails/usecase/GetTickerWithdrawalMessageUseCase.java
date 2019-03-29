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
import com.tokopedia.saldodetails.response.model.GqlWithdrawalTickerResponse;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants.cacheDuration;

public class GetTickerWithdrawalMessageUseCase {

    private GraphqlUseCase graphqlUseCase;
    private Context context;

    @Inject
    public GetTickerWithdrawalMessageUseCase(@ApplicationContext Context context) {
        graphqlUseCase = new GraphqlUseCase();
        this.context = context;
    }

    public void unsubscribe() {
        if (graphqlUseCase != null) {
            graphqlUseCase.unsubscribe();
        }
    }

    public void execute(Subscriber<GraphqlResponse> subscriber) {
        graphqlUseCase.clearRequest();

        HashMap<String, Object> usableRequestMap = new HashMap<>();
        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.query_withdrawal_ticker),
                GqlWithdrawalTickerResponse.class,
                usableRequestMap, false);

        GraphqlCacheStrategy cacheStrategy =
                new GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                        .setExpiryTime(cacheDuration).setSessionIncluded(true).build();

        graphqlUseCase.setCacheStrategy(cacheStrategy);
        graphqlUseCase.addRequest(graphqlRequest);
        graphqlUseCase.execute(subscriber);
    }

}
