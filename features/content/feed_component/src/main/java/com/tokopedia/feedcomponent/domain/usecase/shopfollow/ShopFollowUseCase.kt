package com.tokopedia.feedcomponent.domain.usecase.shopfollow

import com.tokopedia.feedcomponent.data.pojo.shopmutation.ShopFollowModel
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowUseCase.Companion.QUERY
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowUseCase.Companion.QUERY_NAME
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

@GqlQuery(QUERY_NAME, QUERY)
class ShopFollowUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ShopFollowModel>(graphqlRepository) {

    init {
        setGraphqlQuery(ShopFollowUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(ShopFollowModel::class.java)
    }

    companion object {
        private const val SHOP_ID = "shopID"
        private const val ACTION = "action"
        private const val INPUT = "input"
        const val QUERY_NAME = "ShopFollowUseCaseQuery"
        const val QUERY = """
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

        fun createParam(
            shopId: String,
            action: ShopFollowAction,
        ): HashMap<String, Any> {
            return hashMapOf(
                INPUT to hashMapOf(
                    SHOP_ID to shopId,
                    ACTION to action.value
                )
            )
        }
    }

}
