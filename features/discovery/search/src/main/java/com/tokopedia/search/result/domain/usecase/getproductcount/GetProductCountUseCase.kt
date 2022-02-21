package com.tokopedia.search.result.domain.usecase.getproductcount

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

internal class GetProductCountUseCase(
        private val graphqlUseCase: GraphqlUseCase
): UseCase<String>() {

    companion object {
        private const val GET_PRODUCT_COUNT_QUERY: String = """
            query SearchProduct(${'$'}params: String!) {
               searchProduct(params:${'$'}params) {
                   count_text
                }
            }
        """
    }

    override fun createObservable(requestParams: RequestParams): Observable<String> {
        graphqlUseCase.clearRequest()

        graphqlUseCase.addRequest(createGraphqlRequest(requestParams))

        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map { it?.getData(GetProductCountModel::class.java) ?: GetProductCountModel() }
                .map { it?.searchProductCount?.countText ?: "" }
    }

    @GqlQuery("GetProductCount", GET_PRODUCT_COUNT_QUERY)
    private fun createGraphqlRequest(requestParams: RequestParams): GraphqlRequest =
        GraphqlRequest(
            GetProductCount(),
            GetProductCountModel::class.java,
            createVariables(requestParams)
        )

    private fun createVariables(requestParams: RequestParams): Map<String, Any> {
        val variables = mutableMapOf<String, Any>()

        variables[KEY_PARAMS] = UrlParamUtils.generateUrlParamString<Any>(requestParams.parameters)

        return variables
    }

    /**
     * The Model is Private class in here for now, because we only need the String "count_text"
     * Move this model class out if we need more than just "count_text"
     * */
    private class GetProductCountModel {
        @SerializedName("searchProduct")
        @Expose
        val searchProductCount = SearchProductCount()

        class SearchProductCount {
            @SerializedName("count_text")
            @Expose
            val countText = ""
        }
    }
}