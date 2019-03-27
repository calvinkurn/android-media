package com.tokopedia.saldodetails.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.response.model.GqlMerchantCreditDetailsResponse;
import com.tokopedia.saldodetails.response.model.GqlMerchantSaldoDetailsResponse;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class GetMerchantFinancialStatus {

    private GraphqlUseCase graphqlUseCase;
    private Context context;

    @Inject
    public GetMerchantFinancialStatus(@ApplicationContext Context context) {
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
                variables, false);

        graphqlUseCase.addRequest(graphqlRequest);

        GraphqlRequest graphqlMCLRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.query_get_merchant_credit_details),
                GqlMerchantCreditDetailsResponse.class,
                variables, false);

        graphqlUseCase.addRequest(graphqlMCLRequest);

        graphqlUseCase.execute(subscriber);
    }
}
