package com.tokopedia.search.result.domain.usecase.savelastfilter

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS
import com.tokopedia.discovery.common.constants.SearchConstant.SaveLastFilter.INPUT_PARAMS
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class SaveLastFilterUseCase(
    private val graphqlUseCase: GraphqlUseCase
): UseCase<Int>() {

    override fun createObservable(requestParams: RequestParams): Observable<Int> {
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(createGraphqlRequest(requestParams))

        return graphqlUseCase
            .createObservable(RequestParams.create())
            .map(::getStatusCode)
    }

    @GqlQuery("SaveLastFilterQuery", SAVE_LAST_FILTER_QUERY)
    private fun createGraphqlRequest(requestParams: RequestParams): GraphqlRequest =
        GraphqlRequest(
            SaveLastFilterQuery.GQL_QUERY,
            SearchLastFilterResponse::class.java,
            mapOf(KEY_PARAMS to requestParams.parameters[INPUT_PARAMS])
        )

    private fun getStatusCode(graphqlResponse: GraphqlResponse?) =
        graphqlResponse
            ?.getData<SearchLastFilterResponse>(SearchLastFilterResponse::class.java)
            ?.searchLastFilter
            ?.statusCode

    companion object {
        private const val SAVE_LAST_FILTER_QUERY = """
            mutation searchLastFilter(${'$'}params: SearchLastFilterParams!) {
              searchLastFilter(params: ${'$'}params) {
                status_code
              }
            }
        """
    }

    private data class SearchLastFilterResponse(
        @SerializedName("searchLastFilter")
        @Expose
        val searchLastFilter: SearchLastFilter = SearchLastFilter(),
    ) {

        data class SearchLastFilter(
            @SerializedName("status_code")
            @Expose
            val statusCode: Int = 200,
        )
    }
}