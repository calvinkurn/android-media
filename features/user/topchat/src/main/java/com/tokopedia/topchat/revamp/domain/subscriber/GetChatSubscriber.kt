package com.tokopedia.topchat.revamp.domain.subscriber

import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chat_common.util.handleError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.topchat.revamp.domain.mapper.TopChatRoomGetExistingChatMapper
import rx.Subscriber

class GetChatSubscriber(val onErrorGetChat: (Throwable) -> Unit,
                        val onSuccess: (ChatroomViewModel) -> Unit,
                        val mapper: TopChatRoomGetExistingChatMapper = TopChatRoomGetExistingChatMapper()
                        ) : Subscriber<GraphqlResponse>() {
    override fun onNext(graphqlResponse: GraphqlResponse) {
        handleError(graphqlResponse, GetExistingChatPojo::class.java,
                routingOnNext(graphqlResponse), onErrorGetChat)
    }


    private fun routingOnNext(graphqlResponse: GraphqlResponse): (GraphqlResponse) -> Unit {
        return {
            val pojo = graphqlResponse.getData<GetExistingChatPojo>(GetExistingChatPojo::class.java)
            onSuccess(mapper.map(pojo))
        }
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable) {
        onErrorGetChat(e)
    }

}
