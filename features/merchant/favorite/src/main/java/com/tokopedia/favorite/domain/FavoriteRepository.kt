package com.tokopedia.favorite.domain

import com.tokopedia.favorite.domain.model.FavoriteShop
import com.tokopedia.favorite.domain.model.TopAdsShop
import rx.Observable
import java.util.*

/**
 * @author Kulomady on 1/18/17.
 */
interface FavoriteRepository {

    fun getFirstPageFavoriteShop(params: HashMap<String, String>): Observable<FavoriteShop>

    suspend fun suspendGetFirstPageFavoriteShop(params: HashMap<String, String>): FavoriteShop

    fun getFavoriteShop(param: HashMap<String, String>): Observable<FavoriteShop>

    suspend fun suspendGetFavoriteShop(param: HashMap<String, String>): FavoriteShop

    fun getFreshTopAdsShop(params: HashMap<String, Any>): Observable<TopAdsShop>

    suspend fun suspendGetFreshTopAdsShop(params: HashMap<String, Any>): TopAdsShop

    fun getTopAdsShop(params: HashMap<String, Any>): Observable<TopAdsShop>

    suspend fun suspendGetTopAdsShop(params: HashMap<String, Any>): TopAdsShop

}
