package com.tokopedia.review.feature.inbox.buyerreview.data.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.InboxReputationResponseWrapper
import rx.Observable
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 8/14/17.
 */
class InboxReputationMapper @Inject constructor() :
    Func1<GraphqlResponse, Observable<InboxReputationResponseWrapper.Data.Response>> {

    override fun call(graphqlResponse: GraphqlResponse): Observable<InboxReputationResponseWrapper.Data.Response> {
        val response = graphqlResponse.getData<InboxReputationResponseWrapper.Data>(
            InboxReputationResponseWrapper.Data::class.java
        ).response
        return Observable.just(response)
    }
}