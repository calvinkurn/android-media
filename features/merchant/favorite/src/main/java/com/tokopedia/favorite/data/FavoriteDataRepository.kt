package com.tokopedia.favorite.data

import com.tokopedia.favorite.domain.FavoriteRepository
import com.tokopedia.favorite.domain.model.FavoriteShop
import com.tokopedia.favorite.domain.model.TopAdsShop
import java.util.*

/**
 * @author Kulomady on 1/18/17.
 */
class FavoriteDataRepository(private val favoriteFactory: FavoriteFactory) : FavoriteRepository {

    override suspend fun suspendGetFirstPageFavoriteShop(params: HashMap<String, String>): FavoriteShop {
        return favoriteFactory.suspendGetFavoriteShop(params)
    }

    override suspend fun suspendGetFavoriteShop(param: HashMap<String, String>): FavoriteShop {
        return favoriteFactory.suspendGetFavoriteShop(param)
    }

    override suspend fun suspendGetFreshTopAdsShop(params: HashMap<String, Any>): TopAdsShop {
        return favoriteFactory.suspendGetFreshTopAdsShop(params)
    }

    override suspend fun suspendGetTopAdsShop(params: HashMap<String, Any>): TopAdsShop {
        return favoriteFactory.suspendGetTopAdsShop(params)
    }

}
