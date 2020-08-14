package com.tokopedia.favorite.data

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.favorite.data.source.apis.service.TopAdsService
import com.tokopedia.favorite.data.source.cloud.CloudFavoriteShopDataSource
import com.tokopedia.favorite.data.source.cloud.CloudTopAdsShopDataSource
import com.tokopedia.favorite.data.source.local.LocalFavoriteShopDataSource
import com.tokopedia.favorite.data.source.local.LocalTopAdsShopDataSource
import com.tokopedia.favorite.domain.model.FavoriteShop
import com.tokopedia.favorite.domain.model.TopAdsShop
import rx.Observable
import rx.functions.Action1
import rx.functions.Func1
import java.util.*

/**
 * @author Kulomady on 1/18/17.
 */
class FavoriteFactory(
        private val context: Context,
        private val gson: Gson,
        private val topAdsService: TopAdsService
) {

    fun getFavoriteShop(param: HashMap<String, String>?): Observable<FavoriteShop> {
        return CloudFavoriteShopDataSource(context, gson).getFavorite(param, false)
    }

    fun getFavoriteShopFirstPage(params: HashMap<String, String>): Observable<FavoriteShop> {
        return getCloudFavoriteObservable(params)
                .onExceptionResumeNext(localFavoriteObservable.doOnNext(setFavoriteErrorNetwork()))
    }

    fun getTopAdsShop(params: HashMap<String, Any>): Observable<TopAdsShop> {
        return Observable.concat(localTopAdsShopObservable, getCloudTopAdsShopObservable(params))
                .first(isLocalTopAdsShopValid)
    }

    fun getFreshTopAdsShop(params: HashMap<String, Any>): Observable<TopAdsShop> {
        val topAdsShopDataSource = CloudTopAdsShopDataSource(context, gson, topAdsService)
        return topAdsShopDataSource.getTopAdsShop(params)
                .onExceptionResumeNext(localTopAdsShopObservable.doOnNext(setTopAdsShopErrorNetwork()))
    }

    private fun getCloudTopAdsShopObservable(params: HashMap<String, Any>): Observable<TopAdsShop> {
        val topAdsShopDataSource = CloudTopAdsShopDataSource(context, gson, topAdsService)
        return topAdsShopDataSource.getTopAdsShop(params)
    }

    private val localTopAdsShopObservable: Observable<TopAdsShop>
        get() = LocalTopAdsShopDataSource(context, gson).topAdsShop

    private val localFavoriteObservable: Observable<FavoriteShop>
        get() = LocalFavoriteShopDataSource(context, gson).favorite

    private fun getCloudFavoriteObservable(param: HashMap<String, String>): Observable<FavoriteShop> {
        return CloudFavoriteShopDataSource(context, gson).getFavorite(param, true)
    }

    private fun setFavoriteErrorNetwork(): Action1<FavoriteShop> {
        return Action1 { favoriteShop -> favoriteShop.isNetworkError = true }
    }

    private fun setTopAdsShopErrorNetwork(): Action1<TopAdsShop> {
        return Action1 { topAdsShop -> topAdsShop.isNetworkError = true }
    }

    private val isLocalTopAdsShopValid: Func1<TopAdsShop, Boolean>
        get() = Func1 { topAdsShop ->
            (topAdsShop != null &&
                topAdsShop.isDataValid &&
                topAdsShop.topAdsShopItemList != null &&
                topAdsShop.topAdsShopItemList!!.size > 0)
        }

}
