package com.tokopedia.favorite.data.source.local

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.favorite.data.mapper.FavoriteShopMapper
import com.tokopedia.favorite.domain.model.FavoriteShop
import retrofit2.Response
import rx.Observable
import rx.functions.Func1

/**
 * @author Kulomady on 2/13/17.
 */
class LocalFavoriteShopDataSource(private val context: Context,
                                  private val gson: Gson) {

    companion object {
        const val CACHE_KEY_FAVORITE_SHOP = "FAVORITE_SHOP"
    }

    val favorite: Observable<FavoriteShop>
        get() {
            val data = Response.success(
                    PersistentCacheManager.instance.getString(CACHE_KEY_FAVORITE_SHOP, null))
            return Observable.just(data)
                    .map(FavoriteShopMapper(context, gson))
                    .onErrorReturn(nullResponse())
        }

    private fun nullResponse(): Func1<Throwable, FavoriteShop> {
        return Func1 {
            val favoriteShop = FavoriteShop()
            favoriteShop.isDataValid = false
            favoriteShop
        }
    }

}
