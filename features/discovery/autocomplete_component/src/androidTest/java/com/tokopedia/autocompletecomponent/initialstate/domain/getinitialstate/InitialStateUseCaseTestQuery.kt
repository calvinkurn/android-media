package com.tokopedia.autocompletecomponent.initialstate.domain.getinitialstate

import com.tokopedia.autocompletecomponent.initialstate.data.InitialStateUniverse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams

class InitialStateUseCaseTestQuery(
    graphqlUseCase: GraphqlUseCase
): InitialStateUseCase(graphqlUseCase) {

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
        query InitialStateQuery {
            universe_initial_state_test {
                ....
                ....
                ....
            }
        }
    """

    /**
     * Provide a way to get List<InitialStateData> from `graphqlResponse` based on your data class
     */
    override fun getInitialStateList(graphqlResponse: GraphqlResponse): InitialStateUniverse =
        graphqlResponse
            .getData<InitialStateTestResponse>(InitialStateTestResponse::class.java)
            ?.initialStateUniverse
            ?: InitialStateUniverse()
}