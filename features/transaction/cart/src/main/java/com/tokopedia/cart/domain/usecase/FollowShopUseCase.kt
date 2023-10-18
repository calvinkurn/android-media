package com.tokopedia.cart.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.shop.common.domain.interactor.model.favoriteshop.DataFollowShop
import javax.inject.Inject

class FollowShopUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<String, DataFollowShop>(dispatcher.io) {

    companion object {
        const val PARAM_INPUT = "input"
        const val PARAM_SHOP_ID = "shopID"

        const val QUERY_FOLLOW_SHOP = "FollowShopQuery"
    }

    override fun graphqlQuery(): String = FOLLOW_SHOP_QUERY

    @GqlQuery(QUERY_FOLLOW_SHOP, FOLLOW_SHOP_QUERY)
    override suspend fun execute(params: String): DataFollowShop {
        val param = HashMap<String, Any>()
        param[PARAM_INPUT] = mapOf(PARAM_SHOP_ID to params)
        val request = GraphqlRequest(FollowShopQuery(), DataFollowShop::class.java, param)
        val followShopResponse = graphqlRepository.response(listOf(request)).getSuccessData<DataFollowShop>()
        if (followShopResponse.followShop?.isSuccess != true) {
            throw ResponseErrorException(followShopResponse.followShop?.message)
        }
        return followShopResponse
    }
}
