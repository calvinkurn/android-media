package com.tokopedia.chatbot.domain.subscriber

import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.domain.mapper.GetExistingChatMapper
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chat_common.util.handleError
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

/**
 * @author by nisie on 12/12/18.
 */
class GetExistingChatSubscriber(val onErrorGetChat: (Throwable) -> Unit,
                                val onSuccessGetChat: (ChatroomViewModel) -> Unit,
                                val mapper: GetExistingChatMapper = GetExistingChatMapper())
    : Subscriber<GraphqlResponse>() {

    override fun onNext(graphqlResponse: GraphqlResponse) {
        //TODO UNCOMMENT
//        handleError(graphqlResponse, GetExistingChatPojo::class.java,
//                routingOnNext(graphqlResponse), onErrorGetChat)

        //TODO REMOVE THIS
        routingOnNext(graphqlResponse)
    }

    private fun routingOnNext(graphqlResponse: GraphqlResponse): (GraphqlResponse) -> Unit {
        return {
            val pojo = graphqlResponse.getData<GetExistingChatPojo>(GetExistingChatPojo::class.java)

            //TODO REMOVE UNCOMMENT
//            onSuccessGetChat(mapper.map(pojo))
        }
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        //TODO UNCOMMENT
//        onErrorGetChat(e)
    }

}