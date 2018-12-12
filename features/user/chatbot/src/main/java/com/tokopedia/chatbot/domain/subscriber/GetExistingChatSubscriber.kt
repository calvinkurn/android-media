package com.tokopedia.chatbot.domain.subscriber

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.util.handleError
import com.tokopedia.chatbot.domain.pojo.GetChatRepliesPojo
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

/**
 * @author by nisie on 12/12/18.
 */
class GetExistingChatSubscriber(val onErrorGetChat: (Throwable) -> Unit,
                                val onSuccessGetChat: (ArrayList<Visitable<*>>) -> Unit)
    : Subscriber<GraphqlResponse>() {

    override fun onNext(graphqlResponse: GraphqlResponse) {
        handleError(graphqlResponse, GetChatRepliesPojo::class.java,
                routingOnNext(graphqlResponse), onErrorGetChat)
    }

    private fun routingOnNext(graphqlResponse: GraphqlResponse): (GraphqlResponse) -> Unit {
        return {
            //TODO MAPPING
            onSuccessGetChat(arrayListOf())
        }
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        onErrorGetChat(e)
    }

}