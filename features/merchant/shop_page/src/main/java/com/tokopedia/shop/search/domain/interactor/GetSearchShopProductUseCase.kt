package com.tokopedia.shop.search.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.search.ShopSearchProductConstant
import com.tokopedia.shop.search.data.model.UniverseSearchResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetSearchShopProductUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        @Named(ShopSearchProductConstant.UNIVERSE_SEARCH_QUERY)
        private val query: String
) : UseCase<UniverseSearchResponse>() {

    companion object {
        private const val KEY_NAV_SOURCE = "navsource"
        private const val VALUE_NAV_SOURCE = "shoppagenav"
        private const val KEY_SHOP_ID = "shopID"
        private const val KEY_SEARCH_QUERY = "q"

        fun createRequestParam(
                shopID: Int,
                searchQuery: String
        ) = HashMap<String, Any>().apply {
            put(KEY_NAV_SOURCE, VALUE_NAV_SOURCE)
            put(KEY_SHOP_ID, shopID)
            put(KEY_SEARCH_QUERY, searchQuery)
        }
    }

    var requestParams = mapOf<String, Any>()

    override suspend fun executeOnBackground(): UniverseSearchResponse {
        val gqlRequest = GraphqlRequest(query, UniverseSearchResponse::class.java, requestParams)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)
        return graphqlUseCase.executeOnBackground().run {
            getData<UniverseSearchResponse>(UniverseSearchResponse::class.java)
        }
    }
}