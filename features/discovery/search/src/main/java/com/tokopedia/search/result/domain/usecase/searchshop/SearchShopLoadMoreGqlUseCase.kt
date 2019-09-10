package com.tokopedia.search.result.domain.usecase.searchshop

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1
import java.util.*

class SearchShopLoadMoreGqlUseCase(
        private val graphqlRequest: GraphqlRequest,
        private val graphqlUseCase: GraphqlUseCase,
        private val searchShopModelMapper: Func1<GraphqlResponse, SearchShopModel>
): UseCase<SearchShopModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<SearchShopModel> {
        graphqlRequest.variables = createParametersForQuery(requestParams.parameters)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map(searchShopModelMapper)
    }

    private fun createParametersForQuery(parameters: Map<String, Any>): Map<String, Any> {
        val variables = HashMap<String, Any>()

        variables[SearchConstant.GQL.KEY_PARAMS] = UrlParamUtils.generateUrlParamString(parameters)

        return variables
    }
}