package com.tokopedia.shop.search.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.search.data.model.UniverseSearchResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetSearchShopProductUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
) : UseCase<UniverseSearchResponse>() {

    companion object {
        private const val KEY_NAV_SOURCE = "navsource"
        private const val VALUE_NAV_SOURCE = "shoppagenav"
        private const val KEY_SHOP_ID = "shopID"
        private const val KEY_SEARCH_QUERY = "q"
        private const val QUERY = """
            query universe_search(${'$'}navsource: String , ${'$'}shopID: Int, ${'$'}q: String ){
                universe_search(navsource: ${'$'}navsource, shopID : ${'$'}shopID,q: ${'$'}q){
                        data{
                            id
                            name
                            items {
                                location
                                imageURI
                                applink
                                url
                                keyword
                                recom
                                sc
                                isOfficial
                                post_count
                                affiliate_username
                            }
                        }
                   }
            }
        """
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
        val gqlRequest = GraphqlRequest(QUERY, UniverseSearchResponse::class.java, requestParams)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)
        return graphqlUseCase.executeOnBackground().run {
            getData<UniverseSearchResponse>(UniverseSearchResponse::class.java)
        }
    }
}