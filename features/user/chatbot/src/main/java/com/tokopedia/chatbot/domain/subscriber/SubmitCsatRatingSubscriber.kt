package com.tokopedia.chatbot.domain.subscriber

import com.tokopedia.chat_common.util.handleError
import com.tokopedia.chatbot.domain.pojo.csatRating.csatResponse.SubmitCsatGqlResponse
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber


class SubmitCsatRatingSubscriber(val onErrorSubmitRating: (Throwable) -> Unit,
                                 val onSuccess: (String) -> Unit)
    : Subscriber<GraphqlResponse>() {

    override fun onNext(graphqlResponse: GraphqlResponse) {
        val pojo = graphqlResponse.getData<SubmitCsatGqlResponse>(SubmitCsatGqlResponse::class.java)
        handleError(graphqlResponse, SubmitCsatGqlResponse::class.java,
                routingOnNext(graphqlResponse), onErrorSubmitRating)
    }

    private fun routingOnNext(graphqlResponse: GraphqlResponse): (GraphqlResponse) -> Unit {
        return {
            val pojo = graphqlResponse.getData<SubmitCsatGqlResponse>(SubmitCsatGqlResponse::class.java)
            onSuccess(pojo.submitRatingCSAT?.data?.message.toString())

        }
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        onErrorSubmitRating(e)

    }

}