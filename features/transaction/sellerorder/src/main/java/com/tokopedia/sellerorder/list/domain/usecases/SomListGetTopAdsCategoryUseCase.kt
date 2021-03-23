package com.tokopedia.sellerorder.list.domain.usecases

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerorder.list.domain.model.SomListGetShopTopAdsCategoryResponse
import javax.inject.Inject

class SomListGetTopAdsCategoryUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository
) : BaseGraphqlUseCase<Int>(gqlRepository) {

    override suspend fun executeOnBackground(): Int {
        return executeOnBackground(false)
    }

    override suspend fun executeOnBackground(useCache: Boolean): Int {
        val cacheStrategy = getCacheStrategy(useCache)
        val gqlRequest = GraphqlRequest(QUERY, SomListGetShopTopAdsCategoryResponse.Data::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(SomListGetShopTopAdsCategoryResponse.Data::class.java)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<SomListGetShopTopAdsCategoryResponse.Data>()
            return response.topAdsGetShopInfo.data.category
        } else {
            throw RuntimeException(errors.joinToString(", ") { it.message })
        }
    }

    fun setParams(shopId: Int) {
        params.putInt(PARAM_SHOP_ID, shopId)
    }

    companion object {
        private const val PARAM_SHOP_ID = "shop_id"

        private val QUERY = """
            query GetTopAdsGetShopInfo(${'$'}shop_id: Int!) {
              topAdsGetShopInfo(shop_id: ${'$'}shop_id) {
                data {
                  category
                }
              }
            } 
        """.trimIndent()
    }
}