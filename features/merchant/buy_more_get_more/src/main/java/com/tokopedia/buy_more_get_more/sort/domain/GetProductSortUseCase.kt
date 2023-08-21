package com.tokopedia.buy_more_get_more.sort.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.graphql.data.shopsort.GqlShopSortProductResponse
import com.tokopedia.shop.common.graphql.data.shopsort.ShopProductSort
import javax.inject.Inject

class GetProductSortUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
) : GraphqlUseCase<List<ShopProductSort>>(graphqlRepository) {

    private val GQL_QUERY = """
        query GetSortingList {
          GetOfferingSortingList(params: {}) {
            data {
              name
              value
              input_type
              key
            }
          }
        }
    """.trimIndent()

    override suspend fun executeOnBackground(): List<ShopProductSort> {
        val request = GraphqlRequest(GQL_QUERY, GqlShopSortProductResponse::class.java)
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val gqlResponse = graphqlRepository.response(listOf(request), cacheStrategy)
        val error = gqlResponse.getError(GqlShopSortProductResponse::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData<GqlShopSortProductResponse>(GqlShopSortProductResponse::class.java).shopSort.data.sort
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }
}
