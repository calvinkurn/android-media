package com.tokopedia.withdraw.domain.usecase;

import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.withdraw.domain.model.GqlGetBankDataResponse;

import javax.inject.Inject;

import rx.Subscriber;

public class GqlGetBankDataUseCase {

    private GraphqlUseCase graphqlUseCase;
    private boolean isRequesting;
    private String query;

    @Inject
    public GqlGetBankDataUseCase() {
        graphqlUseCase = new GraphqlUseCase();
    }

    public void unsubscribe() {
        if (graphqlUseCase != null) {
            graphqlUseCase.unsubscribe();
        }
    }

    public void execute(Subscriber<GraphqlResponse> subscriber) {
        graphqlUseCase.clearRequest();
        setRequesting(true);
        GraphqlRequest graphqlRequestForUsable = new GraphqlRequest(query,
                GqlGetBankDataResponse.class);
        graphqlUseCase.addRequest(graphqlRequestForUsable);
        graphqlUseCase.execute(subscriber);
    }

    public boolean isRequesting() {
        return isRequesting;
    }

    public void setRequesting(boolean isRequesting) {
        this.isRequesting = isRequesting;
    }

    public void setQuery(String loadRawString) {
        this.query = loadRawString;
    }
}
