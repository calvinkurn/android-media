package com.tokopedia.chatbot.chatbot2.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

/**
 * User receives the emojis on chat , on clicking on any of the emojis calls to ChatbotProvideRatingActivity with code
 * On Receiving the correct result [REQUEST_SUBMIT_FEEDBACK] submits the data with this GQL
 * */

@GqlQuery(
    "submitRatingCSAT",
    com.tokopedia.chatbot.chatbot2.domain.gqlqueries.queries.SUBMIT_CSAT_RATING
)
class SubmitCsatRatingUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<com.tokopedia.chatbot.chatbot2.data.csatRating.csatResponse.SubmitCsatGqlResponse>(graphqlRepository) {

    fun submitCsatRating(
        onSuccess: (com.tokopedia.chatbot.chatbot2.data.csatRating.csatResponse.SubmitCsatGqlResponse) -> Unit,
        onError: kotlin.reflect.KFunction2<Throwable, String, Unit>,
        input: com.tokopedia.chatbot.chatbot2.data.csatRating.csatInput.InputItem,
        messageId: String
    ) {
        try {
            this.setTypeClass(com.tokopedia.chatbot.chatbot2.data.csatRating.csatResponse.SubmitCsatGqlResponse::class.java)
            this.setRequestParams(generateParam(input))
            this.setGraphqlQuery(com.tokopedia.chatbot.chatbot2.domain.gqlqueries.SubmitCsatRatingQuery())

            this.execute(
                { result ->
                    if (result.submitRatingCSAT?.header?.isSuccess == true) {
                        onSuccess(result)
                    } else {
                        onError(
                            Throwable(
                                result.submitRatingCSAT?.header?.messages?.getOrElse(0) { "" } as? String
                                    ?: " "
                            ),
                            messageId
                        )
                    }
                },
                { error ->
                    onError(error, messageId)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable, messageId)
        }
    }

    private fun generateParam(input: com.tokopedia.chatbot.chatbot2.data.csatRating.csatInput.InputItem): Map<String, Any> {
        val requestParams = HashMap<String, Any>()
        requestParams[PARAM_INPUT] = input
        return requestParams
    }

    companion object {
        private val PARAM_INPUT: String = "input"
    }
}
