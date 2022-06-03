package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.homenav.mainnav.data.pojo.favoriteshop.FavoriteShopData
import com.tokopedia.homenav.mainnav.data.pojo.favoriteshop.FavoriteShopParam
import com.tokopedia.homenav.mainnav.data.pojo.favoriteshop.FavoriteShops
import com.tokopedia.homenav.mainnav.domain.model.NavFavoriteShopModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Frenzel on 18/04/22
 */
class GetFavoriteShopsNavUseCase @Inject constructor (
        private val graphqlUseCase: GraphqlUseCase<FavoriteShopData>,
        private val userSession: UserSessionInterface
): UseCase<Pair<List<NavFavoriteShopModel>, Boolean>>(){

    init {
        val query = """
            query GetUserShopFollow(${'$'}input: UserShopFollowParam){
              userShopFollow(input:${'$'}input){
                result {
                  userShopFollowDetail{
                    shopID
                    shopName
                    location
                    logo
                    badge{
                      title
                      imageURL
                    }
                    reputation{
                      score
                      tooltip
                      reputationScore
                      minBadgeScore
                      badge
                      badgeLevel
                    }
                  }
                  haveNext
                  totalCount
                }
              }
            }
        """.trimIndent()
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setRequestParams(generateParam(FavoriteShopParam(userId = userSession.userId)))
        graphqlUseCase.setTypeClass(FavoriteShopData::class.java)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    override suspend fun executeOnBackground(): Pair<List<NavFavoriteShopModel>, Boolean> {
        val responseData = Success(graphqlUseCase.executeOnBackground().result?.favoriteShops?:FavoriteShops())
        val favoriteShopList = mutableListOf<NavFavoriteShopModel>()
        responseData.data.shops?.map {
            favoriteShopList.add(NavFavoriteShopModel(
                id = it.id.orEmpty(),
                name = it.name.orEmpty(),
                imageUrl = it.imageUrl.orEmpty(),
                location = it.location.orEmpty(),
                badgeImageUrl = it.badge?.firstOrNull()?.imageUrl.orEmpty()
            ))
        }
        val totalCount = responseData.data.totalCount?:0
        return Pair(favoriteShopList, totalCount>favoriteShopList.size)
    }

    private fun generateParam(param: FavoriteShopParam): Map<String, Any?> {
        return mapOf(PARAM to param)
    }

    companion object{
        private const val PARAM = "input"
    }
}