package com.tokopedia.search.result.shop.domain.usecase

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.search.result.common.UseCase
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.utils.UrlParamUtils
import java.util.HashMap

internal class SearchShopLoadMoreUseCase(
        private val queryString: String,
        private val graphqlCacheStrategy: GraphqlCacheStrategy,
        private val graphqlRepository: GraphqlRepository
): UseCase<SearchShopModel>() {

    override suspend fun execute(inputParameter: Map<String, Any>): SearchShopModel {
        val graphqlRequest = GraphqlRequest(
                queryString, SearchShopModel::class.java, createParametersForQuery(inputParameter))

        val graphqlResponse = graphqlRepository.getReseponse(listOf(graphqlRequest), graphqlCacheStrategy)

        val error = graphqlResponse.getError(SearchShopModel::class.java)

        if (error == null || error.isEmpty()){
            return graphqlResponse.getData(SearchShopModel::class.java)
        } else {
            throw Exception(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    private fun createParametersForQuery(requestParams: Map<String, Any>): Map<String, Any> {
        val variables = HashMap<String, Any>()

        variables[SearchConstant.GQL.KEY_PARAMS] = UrlParamUtils.generateUrlParamString(requestParams)

        return variables
    }
}