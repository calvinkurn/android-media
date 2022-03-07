package com.tokopedia.autocompletecomponent.initialstate.domain.deleterecentsearch

import com.tokopedia.autocompletecomponent.initialstate.domain.getinitialstate.InitialStateTestResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams

class DeleteRecentSearchUseCaseTestQuery(
    graphqlUseCase: GraphqlUseCase
): DeleteRecentSearchUseCase(graphqlUseCase) {

    /**
     * Create GraphqlRequest based on your query, data class, and params
     */
    override fun createGraphqlRequest(requestParams: RequestParams): GraphqlRequest =
        GraphqlRequest(
            getGraphqlQuery(),
            InitialStateTestResponse::class.java,
            createGraphqlRequestParams(requestParams)
        )

    /**
     * Provide your own query here
     */
    override fun getGraphqlQuery(): String = """
        query DeleteRecentSearchQuery {
            universe_initial_state_test {
                ....
                ....
                ....
            }
        }
    """

    override fun getIsSuccess(graphqlResponse: GraphqlResponse): Boolean = true
}