package com.tokopedia.feedcomponent.domain.usecase.shopfollow

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feedcomponent.data.pojo.shopmutation.ShopFollowModel
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class ShopFollowUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Map<String, Any>, ShopFollowModel>(dispatchers.io) {

    override suspend fun execute(params: Map<String, Any>): ShopFollowModel =
        graphqlRepository.request(
            graphqlQuery(),
            params,
            GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        )

    override fun graphqlQuery(): String = SHOP_FOLLOW_QUERY

    fun createParams(shopId: String) =
        mapOf(
            SHOP_FOLLOW_INPUT to mapOf(
                SHOP_FOLLOW_SHOP_ID to shopId
            )
        )

    fun createParams(shopId: String, action: ShopFollowAction) = mapOf(
        SHOP_FOLLOW_INPUT to mapOf(
            SHOP_FOLLOW_SHOP_ID to shopId,
            SHOP_FOLLOW_ACTION to action.value
        )
    )

    companion object {
        private const val SHOP_FOLLOW_SHOP_ID = "shopID"
        private const val SHOP_FOLLOW_ACTION = "action"
        private const val SHOP_FOLLOW_INPUT = "input"

        const val SHOP_FOLLOW_QUERY = """
            mutation followShop(${'$'}input: ParamFollowShop!) {
              followShop(input:${'$'}input){
                success
                message
                isFirstTime
                isFollowing
                buttonLabel
              }
            }
        """
    }
}
