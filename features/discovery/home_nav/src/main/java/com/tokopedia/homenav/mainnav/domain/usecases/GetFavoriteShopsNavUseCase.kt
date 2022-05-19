package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.homenav.mainnav.data.pojo.favoriteshop.FavoriteShopData
import com.tokopedia.homenav.mainnav.data.pojo.favoriteshop.FavoriteShops
import com.tokopedia.homenav.mainnav.domain.model.NavFavoriteShopModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by Frenzel on 18/04/22
 */
class GetFavoriteShopsNavUseCase @Inject constructor (
        private val graphqlUseCase: GraphqlUseCase<FavoriteShopData>
): UseCase<List<NavFavoriteShopModel>>(){

    init {
        val query = """
            query GetFavoriteShop(${'$'}page:Int, ${'$'}per_page:Int) {
              favorite_shop(page:${'$'}page, per_page:${'$'}per_page){
                shops{
                  image
                  location
                  id
                  name
                  reputation{
                    tooltip
                    badge
                    reputation_score
                    score
                    min_badge_score
                    badge_level
                  }
                  badge{
                    title
                    image_url
                  }
                  stats{
                    total_product
                    total_etalase
                    total_sold
                  }
                }
                paging{
                  current
                  uri_previous
                  uri_next
                }
                error
              } 
            }
        """.trimIndent()
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setRequestParams(setParams(1,5).parameters)
        graphqlUseCase.setTypeClass(FavoriteShopData::class.java)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    override suspend fun executeOnBackground(): List<NavFavoriteShopModel> {
        val responseData = Success(graphqlUseCase.executeOnBackground().favoriteShops?:FavoriteShops())
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
        return favoriteShopList
    }

    private fun setParams(page: Int = 1, itemsPerPage: Int = 5) = RequestParams.create().apply {
        parameters.clear()
        putInt(PARAM_PAGE, page)
        putInt(PARAM_ITEMS_PER_PAGE, itemsPerPage)
    }


    companion object{
        private const val PARAM_PAGE = "page"
        private const val PARAM_ITEMS_PER_PAGE = "per_page"
    }
}