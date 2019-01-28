package com.tokopedia.saldodetails.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.response.model.GqlDepositSummaryResponse;

import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class GetDepositSummaryUseCase {

    private static final String GET_SUMMARY_DEPOSIT = "DepositActivityQuery";
    private GraphqlUseCase graphqlUseCase;
    private Context context;
    private boolean isRequesting;
    private Map<String, Object> variables;

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

        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.query_deposit_details),
                GqlDepositSummaryResponse.class,
                variables, GET_SUMMARY_DEPOSIT);

        graphqlUseCase.addRequest(graphqlRequest);
        graphqlUseCase.execute(subscriber);
    }

    public boolean isRequesting() {
        return isRequesting;
    }

    public void setRequesting(boolean isRequesting) {
        this.isRequesting = isRequesting;
    }
}
