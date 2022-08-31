package com.tokopedia.chatbot.domain.subscriber

import com.tokopedia.chat_common.util.handleError
import com.tokopedia.chatbot.ChatbotConstant
import com.tokopedia.chatbot.domain.pojo.chatrating.SendReasonRatingPojo
import com.tokopedia.chatbot.util.ChatbotNewRelicLogger
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

/**
 * @author by nisie on 21/12/18.
 */

class SendRatingReasonSubscriber(val messageId : String,
                                 val onErrorSendReason: (Throwable) -> Unit,
                                 val onSuccess: (String) -> Unit)
    : Subscriber<GraphqlResponse>() {

    override fun onNext(graphqlResponse: GraphqlResponse) {
        handleError(graphqlResponse, SendReasonRatingPojo::class.java,
                routingOnNext(graphqlResponse), onErrorSendReason)
    }

    private fun routingOnNext(graphqlResponse: GraphqlResponse): (GraphqlResponse) -> Unit {
        return {
            val pojo = graphqlResponse.getData<SendReasonRatingPojo>(SendReasonRatingPojo::class.java)
            onSuccess(pojo.postRatingReason.data.message)
            ChatbotNewRelicLogger.logNewRelic(
                ChatbotConstant.NewRelic.KEY_CHATBOT_RATING_REASON,
                true,
                messageId
            )
        }
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        onErrorSendReason(e)
        ChatbotNewRelicLogger.logNewRelic(
            ChatbotConstant.NewRelic.KEY_CHATBOT_RATING_REASON,
            false,
            messageId,
            e
        )
    }

}