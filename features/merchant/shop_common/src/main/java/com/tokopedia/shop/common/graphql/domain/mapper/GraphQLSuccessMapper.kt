package com.tokopedia.shop.common.graphql.domain.mapper

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.shop.common.graphql.data.GraphQLSuccessMessage

import rx.Observable
import rx.functions.Func1

/**
 * Created by hendry on 08/08/18.
 */

class GraphQLSuccessMapper : Func1<HasGraphQLSuccess, Observable<String>> {

    override fun call(hasGraphQLSuccess: HasGraphQLSuccess): Observable<String> {
        val graphQLSuccessMessage = hasGraphQLSuccess.graphQLSuccessMessage
        if (graphQLSuccessMessage == null) {
            return Observable.just(null)
        }
        return if (graphQLSuccessMessage.isSuccess) {
            Observable.just(graphQLSuccessMessage.message)
        } else {
            Observable.error(MessageErrorException(graphQLSuccessMessage.message))
        }
    }
}
