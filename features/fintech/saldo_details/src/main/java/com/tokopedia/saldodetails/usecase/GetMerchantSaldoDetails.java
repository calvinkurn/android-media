package com.tokopedia.saldodetails.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.response.model.GqlMerchantSaldoDetailsResponse;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

public class GetMerchantSaldoDetails {

    private static final String GET_MERCHANT_SALDO_DETAILS = "";
    private GraphqlUseCase graphqlUseCase;
    private Context context;

    public GetMerchantSaldoDetails(Context context) {
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
        Map<String, Object> variables = new HashMap<>();

        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.query_get_merchant_saldo_details),
                GqlMerchantSaldoDetailsResponse.class,
                variables, GET_MERCHANT_SALDO_DETAILS, false);

        graphqlUseCase.addRequest(graphqlRequest);
        graphqlUseCase.execute(subscriber);
    }
}

