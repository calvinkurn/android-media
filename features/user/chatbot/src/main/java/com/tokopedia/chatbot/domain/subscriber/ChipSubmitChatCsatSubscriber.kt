package com.tokopedia.chatbot.domain.subscriber

import com.tokopedia.chat_common.util.handleError
import com.tokopedia.chatbot.domain.pojo.submitchatcsat.ChipSubmitChatCsatResponse
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

class ChipSubmitChatCsatSubscriber(private val onsubmitingChatCsatSuccess: (String) -> Unit,
                                   private val onsubmitingChatCsatError: (Throwable) -> Unit
) : Subscriber<GraphqlResponse>() {

    override fun onNext(graphqlResponse: GraphqlResponse) {
        handleError(graphqlResponse, ChipSubmitChatCsatResponse::class.java,
                routingOnNext(graphqlResponse), onsubmitingChatCsatError)
    }

    private fun routingOnNext(graphqlResponse: GraphqlResponse): (GraphqlResponse) -> Unit = {
        val pojo = graphqlResponse.getData<ChipSubmitChatCsatResponse>(ChipSubmitChatCsatResponse::class.java)
        onsubmitingChatCsatSuccess(pojo?.chipSubmitChatCSAT?.csatSubmitData?.toasterMessage ?: "")
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        onsubmitingChatCsatError(e)
    }

}
