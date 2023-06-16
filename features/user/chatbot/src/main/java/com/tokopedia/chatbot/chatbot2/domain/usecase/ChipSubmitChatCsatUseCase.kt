package com.tokopedia.chatbot.chatbot2.domain.usecase

import com.tokopedia.chatbot.chatbot2.data.submitchatcsat.ChipSubmitChatCsatInput
import com.tokopedia.chatbot.chatbot2.data.submitchatcsat.ChipSubmitChatCsatResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

/**
 * When receiving attachment type 23 , we create a list of CSAT options , on clicking them we will open
 * ChatbotCsatActivity
 * */
private const val INPUT = "input"

@GqlQuery(
    "ChipSubmitChatCsat",
    com.tokopedia.chatbot.chatbot2.domain.gqlqueries.queries.CHIP_SUBMIT_CHAT_CSAT
)
class ChipSubmitChatCsatUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ChipSubmitChatCsatResponse>(graphqlRepository) {

    fun chipSubmitChatCsat(
        onSuccess: (ChipSubmitChatCsatResponse) -> Unit,
        onError: kotlin.reflect.KFunction2<Throwable, String, Unit>,
        chipSubmitChatCsatInput: ChipSubmitChatCsatInput,
        messageId: String
    ) {
        try {
            this.setTypeClass(ChipSubmitChatCsatResponse::class.java)
            this.setRequestParams(generateParam(chipSubmitChatCsatInput))
            this.setGraphqlQuery(com.tokopedia.chatbot.chatbot2.domain.gqlqueries.ChipSubmitChatCsatQuery())

            this.execute(
                { result ->
                    onSuccess(result)
                },
                { error ->
                    onError(error, messageId)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable, messageId)
        }
    }

    private fun generateParam(chipSubmitChatCsatInput: ChipSubmitChatCsatInput): Map<String, Any> {
        val requestParams = HashMap<String, Any>()
        requestParams[INPUT] = chipSubmitChatCsatInput
        return requestParams
    }
}
