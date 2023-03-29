package com.tokopedia.cart.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.shop.common.domain.interactor.model.favoriteshop.DataFollowShop
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class FollowShopUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository
) : UseCase<DataFollowShop>() {

    companion object {
        const val PARAM_INPUT = "input"
        const val PARAM_SHOP_ID = "shopID"

        const val QUERY_FOLLOW_SHOP = "FollowShopQuery"
    }

    private var params: Map<String, Any> = emptyMap()

    fun buildRequestParams(shopId: String): FollowShopUseCase {
        val variables = HashMap<String, Any>()
        variables[PARAM_INPUT] = mapOf(PARAM_SHOP_ID to shopId)
        params = variables
        return this
    }

    @GqlQuery(QUERY_FOLLOW_SHOP, FOLLOW_SHOP_QUERY)
    override suspend fun executeOnBackground(): DataFollowShop {
        val request = GraphqlRequest(FollowShopQuery(), DataFollowShop::class.java, params)
        val followShopResponse = graphqlRepository.response(listOf(request)).getSuccessData<DataFollowShop>()
        if (followShopResponse.followShop?.isSuccess != true) {
            throw ResponseErrorException(followShopResponse.followShop?.message)
        }
        return followShopResponse
    }
}
