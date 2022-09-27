package com.tokopedia.chatbot.domain.subscriber

import com.tokopedia.chat_common.util.handleError
import com.tokopedia.chatbot.ChatbotConstant
import com.tokopedia.chatbot.domain.pojo.submitoption.SubmitOptionListResponse
import com.tokopedia.chatbot.util.ChatbotNewRelicLogger
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

class ChipSubmitHelpfullQuestionsSubscriber(private val messageId : String,
                                            val onsubmitingHelpfQuestionsError: (Throwable) -> Unit)
    : Subscriber<GraphqlResponse>() {

    override fun onNext(graphqlResponse: GraphqlResponse) {
        handleError(graphqlResponse, SubmitOptionListResponse::class.java,
                routingOnNext(graphqlResponse), onsubmitingHelpfQuestionsError)
    }

    private fun routingOnNext(graphqlResponse: GraphqlResponse): (GraphqlResponse) -> Unit = {
       // just hitting to notify backend
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        onsubmitingHelpfQuestionsError(e)
        ChatbotNewRelicLogger.logNewRelic(
            false,
            messageId,
            ChatbotConstant.NewRelic.KEY_CHATBOT_SUBMIT_HELPFULL_QUESTION,
            e
        )
    }

}