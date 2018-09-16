package com.tokopedia.saldodetails.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.response.model.GqlSetMerchantSaldoStatus;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

public class SetMerchantSaldoStatus {

    private static final String SET_MERCHANT_SALDO_STATUS = "";
    private GraphqlUseCase graphqlUseCase;
    private Context context;

    public SetMerchantSaldoStatus(Context context) {
        graphqlUseCase = new GraphqlUseCase();
        this.context = context;
    }

    public void unsubscribe() {
        if (graphqlUseCase != null) {
            graphqlUseCase.unsubscribe();
        }
    }

    public void execute(boolean isEnabled, Subscriber<GraphqlResponse> subscriber) {
        graphqlUseCase.clearRequest();
        Map<String, Object> variables = new HashMap<>();
        variables.put("enable", isEnabled);

        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.query_set_saldo_status),
                GqlSetMerchantSaldoStatus.class,
                variables, SET_MERCHANT_SALDO_STATUS);

        graphqlUseCase.addRequest(graphqlRequest);
        graphqlUseCase.execute(subscriber);
    }
}
