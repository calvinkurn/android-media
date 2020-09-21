package com.tokopedia.favorite.data

import com.tokopedia.favorite.domain.FavoriteRepository
import com.tokopedia.favorite.domain.model.FavoriteShop
import com.tokopedia.favorite.domain.model.TopAdsShop
import rx.Observable
import java.util.*

/**
 * @author Kulomady on 1/18/17.
 */
class FavoriteDataRepository(private val favoriteFactory: FavoriteFactory) : FavoriteRepository {

    override fun getFirstPageFavoriteShop(params: HashMap<String, String>): Observable<FavoriteShop> {
        return favoriteFactory.getFavoriteShopFirstPage(params)
    }

    override suspend fun suspendGetFirstPageFavoriteShop(params: HashMap<String, String>): FavoriteShop {
        return favoriteFactory.suspendGetFavoriteShop(params)
    }

    override fun getFavoriteShop(param: HashMap<String, String>): Observable<FavoriteShop> {
        return favoriteFactory.getFavoriteShop(param)
    }

    override suspend fun suspendGetFavoriteShop(param: HashMap<String, String>): FavoriteShop {
        return favoriteFactory.suspendGetFavoriteShop(param)
    }

    override fun getFreshTopAdsShop(params: HashMap<String, Any>): Observable<TopAdsShop> {
        return favoriteFactory.getFreshTopAdsShop(params)
    }

    override suspend fun suspendGetFreshTopAdsShop(params: HashMap<String, Any>): TopAdsShop {
        return favoriteFactory.suspendGetFreshTopAdsShop(params)
    }

    override fun getTopAdsShop(params: HashMap<String, Any>): Observable<TopAdsShop> {
        return favoriteFactory.getTopAdsShop(params)
    }

    override suspend fun suspendGetTopAdsShop(params: HashMap<String, Any>): TopAdsShop {
        return favoriteFactory.suspendGetTopAdsShop(params)
    }

}
