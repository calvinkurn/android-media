package com.tokopedia.feedcomponent.domain.usecase.shopmutation

import com.tokopedia.feedcomponent.data.pojo.shopmutation.ShopMutationModel
import com.tokopedia.feedcomponent.domain.usecase.shopmutation.ShopMutationUseCase.Companion.QUERY
import com.tokopedia.feedcomponent.domain.usecase.shopmutation.ShopMutationUseCase.Companion.QUERY_NAME
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

@GqlQuery(QUERY_NAME, QUERY)
class ShopMutationUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ShopMutationModel>(graphqlRepository) {

    init {
        setGraphqlQuery(ShopMutationUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(ShopMutationModel::class.java)
    }

    companion object {
        private const val SHOP_ID = "shopID"
        private const val ACTION = "action"
        private const val INPUT = "input"
        const val QUERY_NAME = "ShopMutationUseCaseQuery"
        const val QUERY = """
            mutation followShop(${'$'}input: ParamFollowShop!) {
              followShop(input:${'$'}input){
                success
                message
                isFollowing
              }
            }
        """

        fun createParam(
            shopId: String,
            action: ShopMutationAction
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
