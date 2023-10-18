package com.tokopedia.contactus.inboxdetail.di

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCaseInterface
import com.tokopedia.usecase.RequestParams
import rx.Observable
import timber.log.Timber

class FakeGraphqlUseCaseInboxDetail : GraphqlUseCaseInterface {

    private var gqlRequest: GraphqlRequest? = null
    private var e: Exception? = null

    override fun clearRequest() {
        gqlRequest = null
    }

    override fun addRequest(requestObject: GraphqlRequest?) {
        gqlRequest = requestObject
    }

    override fun getExecuteObservable(requestParam: RequestParams?): Observable<GraphqlResponse> {
        Timber.d("executing fake usecase")
        e?.let {
            return Observable.error(e)
        }
        if (gqlRequest == null) throw Exception("gql request is null")

        return Observable.error(Throwable("unrecognized query"))
    }

    override fun unsubscribe() {
        // no op
    }

}
