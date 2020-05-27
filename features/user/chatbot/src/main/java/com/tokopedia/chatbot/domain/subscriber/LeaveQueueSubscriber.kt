package com.tokopedia.chatbot.domain.subscriber

import com.tokopedia.chat_common.util.handleError
import com.tokopedia.chatbot.domain.pojo.leavequeue.LeaveQueueResponse
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

class LeaveQueueSubscriber(val onLeaveQueueError: (Throwable) -> Unit,
                           val onSuccess: (String) -> Unit)
    : Subscriber<GraphqlResponse>() {

    override fun onNext(graphqlResponse: GraphqlResponse) {
        handleError(graphqlResponse, LeaveQueueResponse::class.java,
                routingOnNext(graphqlResponse), onLeaveQueueError)
    }

    private fun routingOnNext(graphqlResponse: GraphqlResponse): (GraphqlResponse) -> Unit {
        return {
            val pojo = graphqlResponse.getData<LeaveQueueResponse>(LeaveQueueResponse::class.java)
            pojo?.postLeaveQueue?.leaveQueueHeader?.errorCode?.let { errorcode -> onSuccess(errorcode) }
        }
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        onLeaveQueueError(e)
    }

}