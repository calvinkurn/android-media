package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.homenav.mainnav.data.pojo.favoriteshop.FavoriteShopData
import com.tokopedia.homenav.mainnav.data.pojo.favoriteshop.FavoriteShops
import com.tokopedia.homenav.mainnav.domain.model.NavFavoriteShopModel
import com.tokopedia.homenav.mainnav.domain.usecases.query.GetUserShopFollowQuery
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
        graphqlUseCase.setGraphqlQuery(GetUserShopFollowQuery())
        graphqlUseCase.setRequestParams(generateParam(userId = userSession.userId.toInt()))
        graphqlUseCase.setTypeClass(FavoriteShopData::class.java)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    override suspend fun executeOnBackground(): Pair<List<NavFavoriteShopModel>, Boolean> {
        val responseData = Success(graphqlUseCase.executeOnBackground().userShopFollow?.favoriteShops?:FavoriteShops())
        val favoriteShopList = mutableListOf<NavFavoriteShopModel>()
        responseData.data.shops?.map {
            favoriteShopList.add(NavFavoriteShopModel(
                id = it.id.orEmpty(),
                name = it.name.orEmpty(),
                imageUrl = it.imageUrl.orEmpty(),
                location = it.location.orEmpty(),
                badgeImageUrl = it.badge?.imageUrl.orEmpty()
            ))
        }
        return Pair(favoriteShopList, responseData.data.hasNext?:false)
    }

    private fun generateParam(userId: Int, perPage: Int = PARAM_PER_PAGE_VALUE): Map<String, Any?> {
        return mapOf(PARAM_USER_ID to userId, PARAM_PER_PAGE to PARAM_PER_PAGE_VALUE)
    }

    companion object{
        private const val PARAM_USER_ID = "userID"
        private const val PARAM_PER_PAGE = "perPage"
        private const val PARAM_PER_PAGE_VALUE = 5
    }
}
