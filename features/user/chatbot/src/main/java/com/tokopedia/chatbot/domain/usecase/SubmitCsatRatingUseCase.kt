package com.tokopedia.chatbot.domain.usecase

import com.tokopedia.chatbot.domain.gqlqueries.SubmitCsatRatingQuery
import com.tokopedia.chatbot.domain.gqlqueries.queries.SUBMIT_CSAT_RATING
import com.tokopedia.chatbot.domain.pojo.csatRating.csatInput.InputItem
import com.tokopedia.chatbot.domain.pojo.csatRating.csatResponse.SubmitCsatGqlResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject


@GqlQuery("submitRatingCSAT", SUBMIT_CSAT_RATING)
class SubmitCsatRatingUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<SubmitCsatGqlResponse>(graphqlRepository) {

    fun submitCsatRating(
        onSuccess: (SubmitCsatGqlResponse) -> Unit,
        onError: kotlin.reflect.KFunction2<Throwable, String, Unit>,
        input: InputItem,
        messageId: String
    ) {
        try {
            this.setTypeClass(SubmitCsatGqlResponse::class.java)
            this.setRequestParams(generateParam(input))
            this.setGraphqlQuery(SubmitCsatRatingQuery())

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

    private fun generateParam(input: InputItem): Map<String, Any> {
        val requestParams = HashMap<String, Any>()
        requestParams[PARAM_INPUT] = input
        return requestParams
    }

    companion object {
        private val PARAM_INPUT: String = "input"
    }

}
