package com.tokopedia.search.result.domain.usecase.searchshop

import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_HEADLINE_PARAMS
import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.result.domain.model.SearchShopModel
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.topads.sdk.domain.TopAdsParams
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1
import java.util.HashMap
import com.tokopedia.discovery.common.constants.SearchConstant.SearchShop.HEADLINE
import com.tokopedia.discovery.common.constants.SearchConstant.SearchShop.HEADLINE_ITEM_VALUE
import com.tokopedia.discovery.common.constants.SearchConstant.SearchShop.HEADLINE_TEMPLATE_VALUE

class SearchShopFirstPageGqlUseCase(
        private val graphqlRequest: GraphqlRequest,
        private val graphqlUseCase: GraphqlUseCase,
        private val searchShopModelMapper: Func1<GraphqlResponse, SearchShopModel>
): UseCase<SearchShopModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<SearchShopModel> {
        graphqlRequest.variables = createParametersForQuery(requestParams.parameters)

        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map(searchShopModelMapper)
    }

    private fun createParametersForQuery(parameters: Map<String, Any>): Map<String, Any> {
        val variables = HashMap<String, Any>()

        variables[KEY_PARAMS] = UrlParamUtils.generateUrlParamString(parameters)
        variables[KEY_HEADLINE_PARAMS] = createHeadlineParams(parameters)

        return variables
    }

    private fun createHeadlineParams(parameters: Map<String, Any>): String {
        val headlineParams = HashMap(parameters)
        headlineParams[TopAdsParams.KEY_EP] = HEADLINE
        headlineParams[TopAdsParams.KEY_TEMPLATE_ID] = HEADLINE_TEMPLATE_VALUE
        headlineParams[TopAdsParams.KEY_ITEM] = HEADLINE_ITEM_VALUE

        return UrlParamUtils.generateUrlParamString(headlineParams)
    }
}