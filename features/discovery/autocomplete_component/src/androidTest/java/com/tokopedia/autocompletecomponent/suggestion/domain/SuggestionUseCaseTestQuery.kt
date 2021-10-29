package com.tokopedia.autocompletecomponent.suggestion.domain

import com.tokopedia.autocompletecomponent.suggestion.domain.getsuggestion.SuggestionUseCase
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionUniverse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams

class SuggestionUseCaseTestQuery(
    graphqlUseCase: GraphqlUseCase
): SuggestionUseCase(graphqlUseCase) {

    /**
     * Create GraphqlRequest based on your query, data class, and params
     */
    override fun createGraphqlRequest(requestParams: RequestParams): GraphqlRequest =
        GraphqlRequest(
            getGraphqlQuery(),
            SuggestionTestResponse::class.java,
            createGraphqlRequestParams(requestParams)
        )

    /**
     * Provide your own query here
     */
    override fun getGraphqlQuery(): String = """
        query SuggestionQuery {
            universe_suggestion_test {
                ....
                ....
                ....
            }
        }
    """

    /**
    * Provide a way to get SuggestionUniverse from `graphqlResponse` based on your data class
    */
    override fun getSuggestionUniverse(graphqlResponse: GraphqlResponse): SuggestionUniverse =
        graphqlResponse
            .getData<SuggestionTestResponse>(SuggestionTestResponse::class.java)
            ?.suggestionUniverse
            ?: SuggestionUniverse()
}