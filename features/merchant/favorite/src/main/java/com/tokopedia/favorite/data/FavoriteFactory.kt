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
import kotlin.NoSuchElementException

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

    suspend fun suspendGetFavoriteShop(param: HashMap<String, String>?): FavoriteShop {
        return CloudFavoriteShopDataSource(context, gson).suspendGetFavorite(param, false)
    }

    fun getFavoriteShopFirstPage(params: HashMap<String, String>): Observable<FavoriteShop> {
        return getCloudFavoriteObservable(params)
                .onExceptionResumeNext(localFavoriteObservable.doOnNext(setFavoriteErrorNetwork()))
    }

    fun getTopAdsShop(params: HashMap<String, Any>): Observable<TopAdsShop> {
        return Observable.concat(localTopAdsShopObservable, getCloudTopAdsShopObservable(params))
                .first(isLocalTopAdsShopValid)
    }

    suspend fun suspendGetTopAdsShop(params: HashMap<String, Any>): TopAdsShop {
        var topAdsShop = LocalTopAdsShopDataSource(context, gson).toAdsShop()
        if (isLocalTopAdsShopValid.call(topAdsShop)) {
            return topAdsShop
        }

        topAdsShop = getCloudTopAds(params)
        if (isLocalTopAdsShopValid.call(topAdsShop)) {
            return topAdsShop
        }

        throw NoSuchElementException()
    }

    fun getFreshTopAdsShop(params: HashMap<String, Any>): Observable<TopAdsShop> {
        val topAdsShopDataSource = CloudTopAdsShopDataSource(context, gson, topAdsService)
        return topAdsShopDataSource.getTopAdsShop(params)
                .onExceptionResumeNext(localTopAdsShopObservable.doOnNext(setTopAdsShopErrorNetwork()))
    }

    suspend fun suspendGetFreshTopAdsShop(params: HashMap<String, Any>): TopAdsShop {
        val topAdsShopDataSource = CloudTopAdsShopDataSource(context, gson, topAdsService)
        try {
            return topAdsShopDataSource.suspendGetTopAdsShop(params)
        } catch (e: Exception) {
            return LocalTopAdsShopDataSource(context, gson).toAdsShop()
        }
    }

    private fun getCloudTopAdsShopObservable(params: HashMap<String, Any>): Observable<TopAdsShop> {
        val topAdsShopDataSource = CloudTopAdsShopDataSource(context, gson, topAdsService)
        return topAdsShopDataSource.getTopAdsShop(params)
    }

    private suspend fun getCloudTopAds(params: HashMap<String, Any>): TopAdsShop {
        val topAdsShopDataSource = CloudTopAdsShopDataSource(context, gson, topAdsService)
        return topAdsShopDataSource.suspendGetTopAdsShop(params)
    }

    private val localTopAdsShopObservable: Observable<TopAdsShop>
        get() = LocalTopAdsShopDataSource(context, gson).topAdsShopObs

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
            val isDataValid = topAdsShop.isDataValid
            val itemList = topAdsShop.topAdsShopItemList
            topAdsShop != null && isDataValid && itemList != null && itemList.isNotEmpty()
        }

}
