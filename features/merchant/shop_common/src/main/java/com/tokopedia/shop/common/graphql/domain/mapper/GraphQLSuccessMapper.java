package com.tokopedia.shop.common.graphql.domain.mapper;

import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.shop.common.graphql.data.GraphQLSuccessMessage;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by hendry on 08/08/18.
 */

public class GraphQLSuccessMapper implements Func1<HasGraphQLSuccess, Observable<String>> {

    public Observable<String> call(HasGraphQLSuccess hasGraphQLSuccess) {
        GraphQLSuccessMessage graphQLSuccessMessage = hasGraphQLSuccess.getGraphQLSuccessMessage();
        if (graphQLSuccessMessage.isSuccess()) {
            return Observable.just(graphQLSuccessMessage.getMessage());
        } else {
            return Observable.error(new MessageErrorException(graphQLSuccessMessage.getMessage()));
        }
    }
}
