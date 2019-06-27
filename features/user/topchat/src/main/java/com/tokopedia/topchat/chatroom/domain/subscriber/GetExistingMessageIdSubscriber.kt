package com.tokopedia.topchat.chatroom.domain.subscriber

import com.tokopedia.chat_common.util.handleError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.topchat.chatroom.domain.pojo.GetExistingMessageIdPojo
import rx.Subscriber

/**
 * @author by nisie on 09/01/19.
 */
class GetExistingMessageIdSubscriber(val onErrorGetChat: (Throwable) -> Unit,
                                     val onSuccess: (String) -> Unit
) : Subscriber<GraphqlResponse>() {
    override fun onNext(graphqlResponse: GraphqlResponse) {
        handleError(graphqlResponse, GetExistingMessageIdPojo::class.java,
                routingOnNext(graphqlResponse), onErrorGetChat)
    }


    private fun routingOnNext(graphqlResponse: GraphqlResponse): (GraphqlResponse) -> Unit {
        return {
            val pojo = graphqlResponse.getData<GetExistingMessageIdPojo>(GetExistingMessageIdPojo::class.java)
            onSuccess(pojo.chatExistingChat.messageId)
        }
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable) {
        onErrorGetChat(e)
    }

}
