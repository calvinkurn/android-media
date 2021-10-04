package com.tokopedia.autocompletecomponent.initialstate.domain.getinitialstate

import com.tokopedia.autocompletecomponent.initialstate.data.InitialStateGqlResponse
import com.tokopedia.autocompletecomponent.initialstate.data.InitialStateUniverse
import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateRequestUtils.INITIAL_STATE_QUERY
import com.tokopedia.autocompletecomponent.util.UrlParamHelper
import com.tokopedia.discovery.common.constants.SearchConstant.GQL
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

open class InitialStateUseCase(
    protected val graphqlUseCase: GraphqlUseCase
) : UseCase<InitialStateUniverse>() {

    override fun createObservable(requestParams: RequestParams): Observable<InitialStateUniverse> {
        val graphqlRequest = createGraphqlRequest(requestParams)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequests(listOf(graphqlRequest))

        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map(::getInitialStateList)
    }

    protected open fun createGraphqlRequest(
        requestParams: RequestParams
    ): GraphqlRequest {
        return GraphqlRequest(
            getGraphqlQuery(),
            InitialStateGqlResponse::class.java,
            createGraphqlRequestParams(requestParams)
        )
    }

    @GqlQuery("InitialStateUseCaseQuery", INITIAL_STATE_QUERY)
    protected open fun getGraphqlQuery(): String = InitialStateUseCaseQuery.GQL_QUERY

    protected open fun createGraphqlRequestParams(requestParams: RequestParams) =
        mapOf(GQL.KEY_PARAMS to UrlParamHelper.generateUrlParamString(requestParams.parameters))

    protected open fun getInitialStateList(
        graphqlResponse: GraphqlResponse
    ): InitialStateUniverse =
        graphqlResponse
            .getData<InitialStateGqlResponse>(InitialStateGqlResponse::class.java)
            ?.initialStateUniverse
            ?: InitialStateUniverse()
}