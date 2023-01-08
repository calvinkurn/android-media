package com.tokopedia.chatbot.domain.usecase

import com.tokopedia.chatbot.domain.gqlqueries.ChipSubmitChatCsatQuery
import com.tokopedia.chatbot.domain.gqlqueries.queries.CHIP_SUBMIT_CHAT_CSAT
import com.tokopedia.chatbot.domain.pojo.submitchatcsat.ChipSubmitChatCsatInput
import com.tokopedia.chatbot.domain.pojo.submitchatcsat.ChipSubmitChatCsatResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

private const val INPUT = "input"

@GqlQuery("ChipSubmitChatCsat", CHIP_SUBMIT_CHAT_CSAT)
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
            this.setGraphqlQuery(ChipSubmitChatCsatQuery())

            this.execute(
                { result ->
                    onSuccess(result)
                }, { error ->
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
