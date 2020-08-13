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

    fun getFavoriteShop(param: HashMap<String, String>): Observable<FavoriteShop>

    fun getFreshTopAdsShop(params: HashMap<String, Any>): Observable<TopAdsShop>

    fun getTopAdsShop(params: HashMap<String, Any>): Observable<TopAdsShop>

}