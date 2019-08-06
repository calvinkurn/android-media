package com.tokopedia.search.result.domain.usecase.searchshop

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.search.result.domain.model.SearchShopModel
import com.tokopedia.search.result.domain.usecase.SearchUseCase
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.topads.sdk.domain.TopAdsParams
import java.util.*

class SearchShopFirstPageUseCase(
        private val graphqlUseCase: GraphqlUseCase<SearchShopModel>
): SearchUseCase<SearchShopModel>() {

    override suspend fun executeOnBackground(): SearchShopModel {
        graphqlUseCase.setRequestParams(createParametersForQuery())

        return graphqlUseCase.executeOnBackground()
    }

    private fun createParametersForQuery(): Map<String, Any?> {
        val variables = HashMap<String, Any?>()

        variables[SearchConstant.GQL.KEY_PARAMS] = UrlParamUtils.generateUrlParamString(requestParams)
        variables[SearchConstant.GQL.KEY_HEADLINE_PARAMS] = createHeadlineParams()

        return variables
    }

    private fun createHeadlineParams(): String {
        val headlineParams = HashMap(requestParams)

        headlineParams[TopAdsParams.KEY_EP] = SearchConstant.SearchShop.HEADLINE
        headlineParams[TopAdsParams.KEY_TEMPLATE_ID] = SearchConstant.SearchShop.HEADLINE_TEMPLATE_VALUE
        headlineParams[TopAdsParams.KEY_ITEM] = SearchConstant.SearchShop.HEADLINE_ITEM_VALUE
        headlineParams[TopAdsParams.KEY_SRC] = SearchConstant.SearchShop.ADS_SOURCE

        return UrlParamUtils.generateUrlParamString(headlineParams)
    }
}
