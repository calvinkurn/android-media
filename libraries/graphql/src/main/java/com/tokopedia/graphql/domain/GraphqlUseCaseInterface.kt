package com.tokopedia.graphql.domain

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.RequestParams
import rx.Observable

interface GraphqlUseCaseInterface {
    fun clearRequest()
    fun addRequest(requestObject: GraphqlRequest?)
    fun getExecuteObservable(requestParam: RequestParams?): Observable<GraphqlResponse>
    fun unsubscribe()
}