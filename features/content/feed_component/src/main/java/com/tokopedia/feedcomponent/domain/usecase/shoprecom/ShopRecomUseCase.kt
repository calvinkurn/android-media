package com.tokopedia.feedcomponent.domain.usecase.shoprecom

import com.tokopedia.feedcomponent.shoprecom.model.UserShopRecomModel
import com.tokopedia.feedcomponent.domain.usecase.shoprecom.ShopRecomUseCase.Companion.QUERY
import com.tokopedia.feedcomponent.domain.usecase.shoprecom.ShopRecomUseCase.Companion.QUERY_NAME
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
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

    suspend fun executeOnBackground(
        screenName: String,
        limit: Int,
        cursor: String,
    ): UserShopRecomModel {
        val request = mapOf(
            KEY_SCREEN_NAME to screenName,
            KEY_LIMIT to limit,
            KEY_CURSOR to cursor
        )
        setRequestParams(request)

        return executeOnBackground()
    }

    companion object {
        private const val KEY_SCREEN_NAME = "screenName"
        private const val KEY_LIMIT = "limit"
        private const val KEY_CURSOR = "cursor"

        const val VAL_SCREEN_NAME_USER_PROFILE = "user_profile"
        const val VAL_SCREEN_NAME_FEED_UPDATE = "update_tab"
        const val VAL_LIMIT = 10

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
                  applink
                }
                nextCursor
              }
            }
        """
    }
}
