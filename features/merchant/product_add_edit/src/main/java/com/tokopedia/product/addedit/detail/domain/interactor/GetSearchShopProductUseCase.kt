package com.tokopedia.product.addedit.detail.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.addedit.detail.domain.UniverseSearchResponse
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.UNIVERSE_SEARCH_QUERY
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetSearchShopProductUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        @Named(UNIVERSE_SEARCH_QUERY)
        private val gqlQuery: String
) : UseCase<UniverseSearchResponse>() {

    companion object {
        private const val KEY_SHOP_ID = "shopID"
        private const val KEY_SEARCH_QUERY = "q"

        @JvmStatic
        fun createRequestParam(
                shopID: Int,
                searchQuery: String
        ) = HashMap<String, Any>().apply {
            put(KEY_SHOP_ID, shopID)
            put(KEY_SEARCH_QUERY, searchQuery)
        }
    }

    var requestParams = mapOf<String, Any>()

    override suspend fun executeOnBackground(): UniverseSearchResponse {
        graphqlUseCase.clearRequest()
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.CLOUD_THEN_CACHE).build())

        val gqlRequest = GraphqlRequest(gqlQuery, UniverseSearchResponse::class.java, requestParams)
        graphqlUseCase.addRequest(gqlRequest)
        val gqlResponse = graphqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData<UniverseSearchResponse>(UniverseSearchResponse::class.java)
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }


    fun clearCache() {
        graphqlUseCase.clearCache()
    }
}