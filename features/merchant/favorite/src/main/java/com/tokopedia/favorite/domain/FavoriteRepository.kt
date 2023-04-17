package com.tokopedia.favorite.domain

import com.tokopedia.favorite.domain.model.FavoriteShop
import com.tokopedia.favorite.domain.model.TopAdsShop
import java.util.*

/**
 * @author Kulomady on 1/18/17.
 */
interface FavoriteRepository {

    suspend fun suspendGetFirstPageFavoriteShop(params: HashMap<String, String>): FavoriteShop

    suspend fun suspendGetFavoriteShop(param: HashMap<String, String>): FavoriteShop

    suspend fun suspendGetFreshTopAdsShop(params: HashMap<String, Any>): TopAdsShop

    suspend fun suspendGetTopAdsShop(params: HashMap<String, Any>): TopAdsShop

}
