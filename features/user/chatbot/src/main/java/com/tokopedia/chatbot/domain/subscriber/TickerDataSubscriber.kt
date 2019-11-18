package com.tokopedia.chatbot.domain.subscriber

import com.tokopedia.chat_common.util.handleError
import com.tokopedia.chatbot.data.TickerData.TickerData
import com.tokopedia.chatbot.data.TickerData.TickerDataResponse
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

class TickerDataSubscriber(val onError: (Throwable) -> Unit,
                           val onSuccess: (TickerData) -> Unit)
    : Subscriber<GraphqlResponse>() {

    override fun onNext(graphqlResponse: GraphqlResponse) {
        handleError(graphqlResponse, TickerDataResponse::class.java,
                routingOnNext(graphqlResponse), onError)
    }

    private fun routingOnNext(graphqlResponse: GraphqlResponse): (GraphqlResponse) -> Unit {
        return {
            val pojo = graphqlResponse.getData<TickerDataResponse>(TickerDataResponse::class.java)
            pojo?.chipGetActiveTickerV4?.data?.let { tickerData -> onSuccess(tickerData) }
        }
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        onError(e)
    }

}