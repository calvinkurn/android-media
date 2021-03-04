package com.tokopedia.chatbot.domain.subscriber

import com.tokopedia.chat_common.util.handleError
import com.tokopedia.chatbot.domain.pojo.leavequeue.LeaveQueueResponse
import com.tokopedia.chatbot.domain.pojo.submitoption.SubmitOptionListResponse
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

class ChipSubmitHelpfullQuestionsSubscriber(val onsubmitingHelpfQuestionsError: (Throwable) -> Unit)
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
    }

}