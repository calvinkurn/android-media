package com.tokopedia.shop.common.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.data.response.GqlShopSortProductResponse
import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSort
import javax.inject.Inject

class GqlGetShopSortUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : GraphqlUseCase<List<ShopProductSort>>(graphqlRepository) {

    private val GQL_QUERY: String = """
            query getShopSort {
              shopSort {
                data {
                  sort {
                    name
                    key
                    value
                    input_type
                  }
                }
              }
            }
        """.trimIndent()

    override suspend fun executeOnBackground(): List<ShopProductSort> {
        val request = GraphqlRequest(GQL_QUERY, GqlShopSortProductResponse::class.java)
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val gqlResponse = graphqlRepository.getReseponse(listOf(request), cacheStrategy)
        val error = gqlResponse.getError(GqlShopSortProductResponse::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData<GqlShopSortProductResponse>(GqlShopSortProductResponse::class.java).shopSort.data.sort
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }
}