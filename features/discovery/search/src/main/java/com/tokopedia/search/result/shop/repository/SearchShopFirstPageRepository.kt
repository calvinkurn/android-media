package com.tokopedia.search.result.shop.repository

import com.tokopedia.discovery.common.RepositoryKt
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.topads.sdk.domain.TopAdsParams
import com.tokopedia.usecase.RequestParams
import java.util.HashMap

class SearchShopFirstPageRepository(
        private val graphqlUseCase: GraphqlUseCase<SearchShopModel>,
): RepositoryKt<RequestParams, SearchShopModel> {

    override fun getResponse(inputParams: RequestParams): SearchShopModel {

    }

    private fun createParametersForQuery(requestParams: Map<String, String>): Map<String, Any?> {
        val variables = HashMap<String, Any?>()

        variables[SearchConstant.GQL.KEY_PARAMS] = UrlParamUtils.generateUrlParamString(requestParams)
        variables[SearchConstant.GQL.KEY_HEADLINE_PARAMS] = createHeadlineParams(requestParams)

        return variables
    }

    private fun createHeadlineParams(requestParams: Map<String, String>): String {
        val headlineParams = HashMap(requestParams)

        headlineParams[TopAdsParams.KEY_EP] = SearchConstant.SearchShop.HEADLINE
        headlineParams[TopAdsParams.KEY_TEMPLATE_ID] = SearchConstant.SearchShop.HEADLINE_TEMPLATE_VALUE
        headlineParams[TopAdsParams.KEY_ITEM] = SearchConstant.SearchShop.HEADLINE_ITEM_VALUE
        headlineParams[TopAdsParams.KEY_SRC] = SearchConstant.SearchShop.ADS_SOURCE

        return UrlParamUtils.generateUrlParamString(headlineParams)
    }
}