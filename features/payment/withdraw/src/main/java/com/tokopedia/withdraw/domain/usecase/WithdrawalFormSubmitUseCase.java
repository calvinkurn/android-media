package com.tokopedia.withdraw.domain.usecase;

import com.tokopedia.graphql.domain.GraphqlUseCase;

public class WithdrawalFormSubmitUseCase {
    private GraphqlUseCase graphqlUseCase;
    public void unsubscribe() {
        if (graphqlUseCase != null) {
            graphqlUseCase.unsubscribe();
        }
    }
}
