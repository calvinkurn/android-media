package com.tokopedia.chatbot.domain.subscriber

import com.tokopedia.chat_common.util.handleError
import com.tokopedia.chatbot.domain.pojo.ratinglist.ChipGetChatRatingListResponse
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

class ChipGetChatRatingListSubscriber (val onGetChatRatingListError: (Throwable) -> Unit,
                                       val onSuccess: (ChipGetChatRatingListResponse.ChipGetChatRatingList?) -> Unit)
    : Subscriber<GraphqlResponse>() {

    override fun onNext(graphqlResponse: GraphqlResponse) {
        handleError(graphqlResponse, ChipGetChatRatingListResponse::class.java,
                routingOnNext(graphqlResponse), onGetChatRatingListError)
    }

    private fun routingOnNext(graphqlResponse: GraphqlResponse): (GraphqlResponse) -> Unit {
        return {
            val pojo = graphqlResponse.getData<ChipGetChatRatingListResponse>(ChipGetChatRatingListResponse::class.java)
            onSuccess(pojo.chipGetChatRatingList)
        }
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        onGetChatRatingListError(e)
    }

}