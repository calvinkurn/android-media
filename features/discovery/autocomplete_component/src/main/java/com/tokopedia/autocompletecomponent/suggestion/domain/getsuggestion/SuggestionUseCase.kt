package com.tokopedia.autocompletecomponent.suggestion.domain.getsuggestion

import com.tokopedia.autocompletecomponent.suggestion.domain.SuggestionRequestUtils.SUGGESTION_QUERY
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionResponse
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionUniverse
import com.tokopedia.autocompletecomponent.util.UrlParamHelper
import com.tokopedia.discovery.common.constants.SearchConstant.GQL
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

open class SuggestionUseCase(
    protected val graphqlUseCase: GraphqlUseCase
) : UseCase<SuggestionUniverse>() {

    override fun createObservable(requestParams: RequestParams): Observable<SuggestionUniverse> {
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(createGraphqlRequest(requestParams))

        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map(::getSuggestionUniverse)
    }

    protected open fun createGraphqlRequest(requestParams: RequestParams): GraphqlRequest {
        return GraphqlRequest(
            getGraphqlQuery(),
            SuggestionResponse::class.java,
            createGraphqlRequestParams(requestParams)
        )
    }

    @GqlQuery("SuggestionUseCaseQuery", SUGGESTION_QUERY)
    protected open fun getGraphqlQuery() = SuggestionUseCaseQuery.GQL_QUERY

    protected open fun createGraphqlRequestParams(requestParams: RequestParams): Map<String, String> {
        val params = UrlParamHelper.generateUrlParamString(requestParams.parameters)

        return mapOf(GQL.KEY_PARAMS to params)
    }

    protected open fun getSuggestionUniverse(graphqlResponse: GraphqlResponse): SuggestionUniverse =
        graphqlResponse
            .getData<SuggestionResponse>(SuggestionResponse::class.java)
            ?.suggestionUniverse
            ?: SuggestionUniverse()
}