package com.tokopedia.feedcomponent.domain.usecase.shopfollow

import com.tokopedia.feedcomponent.data.pojo.shopmutation.ShopFollowModel
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowUseCase.Companion.SHOP_FOLLOW_QUERY
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowUseCase.Companion.SHOP_FOLLOW_QUERY_NAME
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

@GqlQuery(SHOP_FOLLOW_QUERY_NAME, SHOP_FOLLOW_QUERY)
class ShopFollowUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ShopFollowModel>(graphqlRepository) {

    init {
        setGraphqlQuery(ShopFollowUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(ShopFollowModel::class.java)
    }

    suspend fun executeOnBackground(
        shopId: String,
        action: ShopFollowAction,
    ): ShopFollowModel {
        val request = mapOf(
            SHOP_FOLLOW_INPUT to mapOf(
                SHOP_FOLLOW_SHOP_ID to shopId,
                SHOP_FOLLOW_ACTION to action.value
            )
        )
        setRequestParams(request)

        return executeOnBackground()
    }

    suspend fun executeOnBackground(
        shopId: String,
    ): ShopFollowModel {
        val request = mapOf(
            SHOP_FOLLOW_INPUT to mapOf(
                SHOP_FOLLOW_SHOP_ID to shopId,
            )
        )
        setRequestParams(request)

        return executeOnBackground()
    }

    companion object {
        private const val SHOP_FOLLOW_SHOP_ID = "shopID"
        private const val SHOP_FOLLOW_ACTION = "action"
        private const val SHOP_FOLLOW_INPUT = "input"
        const val SHOP_FOLLOW_QUERY_NAME = "ShopFollowUseCaseQuery"
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
