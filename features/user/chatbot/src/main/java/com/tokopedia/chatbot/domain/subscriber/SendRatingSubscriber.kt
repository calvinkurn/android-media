package com.tokopedia.chatbot.domain.subscriber

import com.tokopedia.chat_common.util.handleError
import com.tokopedia.chatbot.ChatbotConstant
import com.tokopedia.chatbot.domain.pojo.chatrating.SendRatingPojo
import com.tokopedia.chatbot.util.ChatbotNewRelicLogger
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

/**
 * @author by nisie on 21/12/18.
 */

class SendRatingSubscriber(val messageId : String,
                           val onErrorSendRating: (Throwable) -> Unit,
                           val onSuccessSendRating: (SendRatingPojo) -> Unit)
    : Subscriber<GraphqlResponse>() {

    override fun onNext(graphqlResponse: GraphqlResponse) {
        handleError(graphqlResponse, SendRatingPojo::class.java,
                routingOnNext(graphqlResponse), onErrorSendRating)
    }

    private fun routingOnNext(graphqlResponse: GraphqlResponse): (GraphqlResponse) -> Unit {
        return {
            val pojo = graphqlResponse.getData<SendRatingPojo>(SendRatingPojo::class.java)
            onSuccessSendRating(pojo)
        }
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        onErrorSendRating(e)
        ChatbotNewRelicLogger.logNewRelic(
            false,
            messageId,
            ChatbotConstant.NewRelic.KEY_CHATBOT_SEND_RATING,
            e
        )
    }

}