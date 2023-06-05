package com.tokopedia.chatbot.chatbot2.domain.usecase

import com.tokopedia.chatbot.chatbot2.data.submitoption.SubmitOptionInput
import com.tokopedia.chatbot.chatbot2.data.submitoption.SubmitOptionListResponse
import com.tokopedia.chatbot.chatbot2.domain.gqlqueries.queries.CHIP_SUBMIT_HELPFULL_QUESTION_MUTATION_QUERY
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

/**
 * When receiving the attachment type 22, we create a list of Helpfull Questions , whenever we click any one of them ,
 * we hit this GQL,
 * */
@GqlQuery("SubmitHelpfullQuestion", CHIP_SUBMIT_HELPFULL_QUESTION_MUTATION_QUERY)
class ChipSubmitHelpfulQuestionsUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<SubmitOptionListResponse>(graphqlRepository) {

    fun chipSubmitHelpfulQuestions(
        onError: kotlin.reflect.KFunction2<Throwable, String, Unit>,
        submitOptionInput: SubmitOptionInput,
        messageId: String
    ) {
        try {
            this.setTypeClass(SubmitOptionListResponse::class.java)
            this.setRequestParams(generateParam(submitOptionInput))
            this.setGraphqlQuery(com.tokopedia.chatbot.chatbot2.domain.gqlqueries.ChipSubmitHelpfulQuestionsQuery())

            this.execute(
                {
                },
                { error ->
                    onError(error, messageId)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable, messageId)
        }
    }

    private fun generateParam(submitOptionInput: SubmitOptionInput): Map<String, Any> {
        val requestParams = HashMap<String, Any>()
        requestParams[INPUT] = submitOptionInput
        return requestParams
    }

    companion object {
        const val INPUT = "input"
    }
}
