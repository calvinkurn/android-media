package com.tokopedia.shop.common.graphql.domain.mapper

import android.text.TextUtils

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.shop.common.graphql.data.GraphQLDataError
import com.tokopedia.shop.common.graphql.data.GraphQLResult

import rx.Observable
import rx.functions.Func1

/**
 * Created by hendry on 08/08/18.
 */

class GraphQLResultMapper<T> : Func1<HasGraphQLResult<T>, Observable<T>> {

    override fun call(graphQLResultParent: HasGraphQLResult<T>): Observable<T> {
        val graphQLResult: GraphQLResult<T>? = graphQLResultParent.result
        if (graphQLResult == null) {
            return Observable.error(RuntimeException());
        }
        val result:T? = graphQLResult.result
        val graphQLDataError:GraphQLDataError? = graphQLResult.graphQLDataError
        return if (graphQLDataError == null || TextUtils.isEmpty(graphQLDataError.message)) {
            Observable.just(result)
        } else {
            Observable.error(MessageErrorException(graphQLDataError.message))
        }
    }
}
