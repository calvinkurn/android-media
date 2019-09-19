package com.tokopedia.stickylogin.domain.subcriber

import com.tokopedia.stickylogin.data.TickerPojo
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

class StickyLoginSubscriber(
    val success: (TickerPojo.TickerResponse) -> Unit,
    val error: (Throwable) -> Unit
): Subscriber<GraphqlResponse>() {

    override fun onNext(graphqlResponse: GraphqlResponse) {
        val response = graphqlResponse.getData<TickerPojo.TickerResponse>(TickerPojo.TickerResponse::class.java)
        success(response)
    }

    override fun onError(e: Throwable) {
        error(e)
    }

    override fun onCompleted() {

    }
}