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

    override fun getFavoriteShop(param: HashMap<String, String>): Observable<FavoriteShop> {
        return favoriteFactory.getFavoriteShop(param)
    }

    override fun getFreshTopAdsShop(params: HashMap<String, Any>): Observable<TopAdsShop> {
        return favoriteFactory.getFreshTopAdsShop(params)
    }

    override fun getTopAdsShop(params: HashMap<String, Any>): Observable<TopAdsShop> {
        return favoriteFactory.getTopAdsShop(params)
    }

}