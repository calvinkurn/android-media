package com.tokopedia.search.result.shop.domain.usecase

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.topads.sdk.domain.TopAdsParams
import com.tokopedia.usecase.coroutines.UseCase
import java.util.HashMap

internal class SearchShopFirstPageUseCase(
        private val queryString: String,
        private val graphqlCacheStrategy: GraphqlCacheStrategy,
        private val graphqlRepository: GraphqlRepository
): UseCase<SearchShopModel>() {

    override suspend fun executeOnBackground(): SearchShopModel {
        val graphqlRequest = GraphqlRequest(
                queryString, SearchShopModel::class.java, createParametersForQuery())

        val graphqlResponse = graphqlRepository.getReseponse(listOf(graphqlRequest), graphqlCacheStrategy)

        val error = graphqlResponse.getError(SearchShopModel::class.java)

        if (error == null || error.isEmpty()){
            return graphqlResponse.getData(SearchShopModel::class.java)
        } else {
            throw Exception(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    private fun createParametersForQuery(): Map<String, Any> {
        val variables = HashMap<String, Any>()

        variables[SearchConstant.GQL.KEY_PARAMS] = UrlParamUtils.generateUrlParamString(useCaseRequestParams.parameters)
        variables[SearchConstant.GQL.KEY_HEADLINE_PARAMS] = createHeadlineParams(useCaseRequestParams.parameters)

        return variables
    }

    private fun createHeadlineParams(requestParams: Map<String, Any>): String {
        val headlineParams = HashMap(requestParams)

        headlineParams[TopAdsParams.KEY_EP] = SearchConstant.SearchShop.HEADLINE
        headlineParams[TopAdsParams.KEY_TEMPLATE_ID] = SearchConstant.SearchShop.HEADLINE_TEMPLATE_VALUE
        headlineParams[TopAdsParams.KEY_ITEM] = SearchConstant.SearchShop.HEADLINE_ITEM_VALUE
        headlineParams[TopAdsParams.KEY_SRC] = SearchConstant.SearchShop.ADS_SOURCE

        return UrlParamUtils.generateUrlParamString(headlineParams)
    }
}