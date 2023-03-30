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
        onError: (Throwable) -> Unit,
        chipSubmitChatCsatInput: ChipSubmitChatCsatInput
    ) {
        try {
            this.setTypeClass(ChipSubmitChatCsatResponse::class.java)
            this.setRequestParams(generateParam(chipSubmitChatCsatInput))
            this.setGraphqlQuery(ChipSubmitChatCsatQuery())

            this.execute(
                { result ->
                    onSuccess(result)
                },
                { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun generateParam(chipSubmitChatCsatInput: ChipSubmitChatCsatInput): Map<String, Any> {
        val requestParams = HashMap<String, Any>()
        requestParams[INPUT] = chipSubmitChatCsatInput
        return requestParams
    }
}
