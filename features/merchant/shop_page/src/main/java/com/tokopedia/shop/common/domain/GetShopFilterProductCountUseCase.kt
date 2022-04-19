package com.tokopedia.shop.common.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.constant.ShopPageGqlQueryConstant
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import javax.inject.Inject

class GetShopFilterProductCountUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : GraphqlUseCase<Int>(graphqlRepository) {

    companion object {
        private const val PARAM_SHOP_ID = "shopId"
        private const val PARAM_FILTER = "filter"
        fun createParams(
                shopId: String,
                filter: ShopProductFilterInput
        ) = mapOf(
                PARAM_SHOP_ID to shopId,
                PARAM_FILTER to filter
        )

    }

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): Int {
        val request = GraphqlRequest(ShopPageGqlQueryConstant.getShopFilterProductCountQuery(), ShopProduct.Response::class.java, params)
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val gqlResponse = graphqlRepository.response(listOf(request), cacheStrategy)
        val error = gqlResponse.getError(ShopProduct.Response::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData<ShopProduct.Response>(ShopProduct.Response::class.java).getShopProduct.totalData
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }
}