package com.tokopedia.withdraw.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.withdraw.R;
import com.tokopedia.withdraw.domain.model.BaseFormSubmitResponse;

import java.util.HashMap;

import rx.Subscriber;

public class WithdrawalFormSubmitUseCase {
    private GraphqlUseCase graphqlUseCase;
    public void unsubscribe() {
        if (graphqlUseCase != null) {
            graphqlUseCase.unsubscribe();
        }
    }

    public void execute(Subscriber<GraphqlResponse> subscriber, Context context, HashMap<String, Object> params) {
        graphqlUseCase.clearRequest();
        GraphqlRequest graphqlRequestForUsable = new GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(), R.raw.query_success_page),
                BaseFormSubmitResponse.class, params);
        graphqlUseCase.addRequest(graphqlRequestForUsable);
        graphqlUseCase.execute(subscriber);
    }
}
