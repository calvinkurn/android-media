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
import com.tokopedia.saldodetails.response.model.GqlSaldoBalanceResponse;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants.cacheDuration;

public class GetSaldoBalanceUseCase {

    private static final String GET_SALDO_BALANCE = "SaldoQuery";
    private GraphqlUseCase graphqlUseCase;
    private Context context;
    private boolean isRequesting;

    @Inject
    public GetSaldoBalanceUseCase(@ApplicationContext Context context) {
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
        setRequesting(true);

        HashMap<String, Object> usableRequestMap = new HashMap<>();
        GraphqlRequest graphqlRequestForUsable = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.query_saldo_balance),
                GqlSaldoBalanceResponse.class,
                usableRequestMap, GET_SALDO_BALANCE);
        GraphqlCacheStrategy cacheStrategy =
                new GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                        .setExpiryTime(cacheDuration).setSessionIncluded(true).build();
        graphqlUseCase.setCacheStrategy(cacheStrategy);
        graphqlUseCase.addRequest(graphqlRequestForUsable);
        graphqlUseCase.execute(subscriber);
    }

    public boolean isRequesting() {
        return isRequesting;
    }

    public void setRequesting(boolean isRequesting) {
        this.isRequesting = isRequesting;
    }
}
