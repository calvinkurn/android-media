package com.tokopedia.shop.common.graphql.domain.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.shop.common.graphql.data.GraphQLDataError;
import com.tokopedia.shop.common.graphql.data.GraphQLResult;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by hendry on 08/08/18.
 */

public class GraphQLResultMapper<T> implements Func1<HasGraphQLResult<T>, Observable<T>> {

    public Observable<T> call(HasGraphQLResult<T> graphQLResultParent) {
        GraphQLResult<T> graphQLResult = graphQLResultParent.getResult();
        T result = graphQLResult.getResult();
        GraphQLDataError graphQLDataError = graphQLResult.getGraphQLDataError();
        if (graphQLDataError == null || TextUtils.isEmpty(graphQLDataError.getMessage())) {
            return Observable.just(result);
        } else {
            return Observable.error(new MessageErrorException(graphQLDataError.getMessage()));
        }
    }
}
