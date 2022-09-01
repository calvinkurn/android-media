package com.tokopedia.chatbot.domain.subscriber

import com.tokopedia.chat_common.util.handleError
import com.tokopedia.chatbot.ChatbotConstant
import com.tokopedia.chatbot.domain.pojo.csatRating.csatResponse.SubmitCsatGqlResponse
import com.tokopedia.chatbot.util.ChatbotNewRelicLogger
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber


class SubmitCsatRatingSubscriber(val messageId : String,
                                 val onErrorSubmitRating: (Throwable) -> Unit,
                                 val onSuccess: (String) -> Unit)
    : Subscriber<GraphqlResponse>() {

    override fun onNext(graphqlResponse: GraphqlResponse) {
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
        ChatbotNewRelicLogger.logNewRelic(
            false,
            messageId,
            ChatbotConstant.NewRelic.KEY_CHATBOT_CSAT_RATING,
            e
        )
    }

}