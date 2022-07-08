package com.tokopedia.people.domains

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.people.domains.ShopRecomUseCase.Companion.QUERY
import com.tokopedia.people.domains.ShopRecomUseCase.Companion.QUERY_NAME
import com.tokopedia.people.model.UserShopRecomModel
import javax.inject.Inject

@GqlQuery(QUERY_NAME, QUERY)
class ShopRecomUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<UserShopRecomModel>(graphqlRepository) {

    init {
        setGraphqlQuery(ShopRecommendationUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(UserShopRecomModel::class.java)
    }

    companion object {
        private const val KEY_SCREEN_NAME = "screenName"
        private const val KEY_LIMIT = "limit"
        private const val KEY_CURSOR = "cursor"
        private const val VALUE_SCREEN_NAME = "user_profile"
        private const val VALUE_LIMIT = 10
        private const val VALUE_CURSOR = ""
        const val QUERY_NAME = "ShopRecommendationUseCaseQuery"
        const val QUERY = """
            query FeedXRecomWidget(
                ${"$${KEY_SCREEN_NAME}"}: String!, 
                ${"$${KEY_LIMIT}"}: Int!, 
                ${"$$KEY_CURSOR"}: String!
            ){
            feedXRecomWidget(req: {
                ${KEY_SCREEN_NAME}: ${"$$KEY_SCREEN_NAME"}, 
                ${KEY_LIMIT}: ${"$$KEY_LIMIT"}, 
                ${KEY_CURSOR}: ${"$$KEY_CURSOR"}}) {
                isShown
                title
                items {
                  type
                  id
                  encryptedID
                  name
                  nickname
                  badgeImageURL
                  logoImageURL
                }
                nextCursor
              }
            }
        """

        fun createParam(
            screenName: String = VALUE_SCREEN_NAME,
            limit: Int = VALUE_LIMIT,
            cursor: String = VALUE_CURSOR
        ) = mapOf<String, Any>(
            KEY_SCREEN_NAME to screenName,
            KEY_LIMIT to limit,
            KEY_CURSOR to cursor
        )
    }

}
