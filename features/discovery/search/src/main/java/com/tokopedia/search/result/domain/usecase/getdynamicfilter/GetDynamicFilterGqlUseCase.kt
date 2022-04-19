package com.tokopedia.search.result.domain.usecase.getdynamicfilter

import rx.functions.Func1
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.discovery.common.constants.SearchConstant.GQL
import com.tokopedia.filter.common.helper.FilterSortProduct
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.result.data.response.GqlDynamicFilterResponse
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

internal class GetDynamicFilterGqlUseCase(
        private val graphqlUseCase: GraphqlUseCase,
        private val dynamicFilterModelGqlMapper: Func1<GraphqlResponse?, DynamicFilterModel?>
) : UseCase<DynamicFilterModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<DynamicFilterModel> {
        val graphqlRequest = createGraphqlRequest(requestParams)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map(dynamicFilterModelGqlMapper)
    }

    private fun createGraphqlRequest(requestParams: RequestParams): GraphqlRequest =
        GraphqlRequest(
            FilterSortProduct.GQL_QUERY,
            GqlDynamicFilterResponse::class.java,
            createParametersForQuery(requestParams.parameters)
        )

    private fun createParametersForQuery(parameters: Map<String?, Any?>): Map<String, Any> =
        mapOf(
            GQL.KEY_PARAMS to UrlParamUtils.generateUrlParamString(parameters)
        )
}