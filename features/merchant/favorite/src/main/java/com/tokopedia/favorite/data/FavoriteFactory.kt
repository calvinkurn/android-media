package com.tokopedia.favorite.data

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.favorite.data.source.apis.service.TopAdsService
import com.tokopedia.favorite.data.source.cloud.CloudFavoriteShopDataSource
import com.tokopedia.favorite.data.source.cloud.CloudTopAdsShopDataSource
import com.tokopedia.favorite.data.source.local.LocalTopAdsShopDataSource
import com.tokopedia.favorite.domain.model.FavoriteShop
import com.tokopedia.favorite.domain.model.TopAdsShop
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

    suspend fun suspendGetFavoriteShop(param: HashMap<String, String>?): FavoriteShop {
        return CloudFavoriteShopDataSource(context).suspendGetFavorite(param, false)
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

    suspend fun suspendGetFreshTopAdsShop(params: HashMap<String, Any>): TopAdsShop {
        val topAdsShopDataSource = CloudTopAdsShopDataSource(context, gson, topAdsService)
        try {
            return topAdsShopDataSource.suspendGetTopAdsShop(params)
        } catch (e: Exception) {
            return LocalTopAdsShopDataSource(context, gson).toAdsShop()
        }
    }

    private suspend fun getCloudTopAds(params: HashMap<String, Any>): TopAdsShop {
        val topAdsShopDataSource = CloudTopAdsShopDataSource(context, gson, topAdsService)
        return topAdsShopDataSource.suspendGetTopAdsShop(params)
    }

    private val isLocalTopAdsShopValid: Func1<TopAdsShop, Boolean>
        get() = Func1 { topAdsShop ->
            val isDataValid = topAdsShop.isDataValid
            val itemList = topAdsShop.topAdsShopItemList
            topAdsShop != null && isDataValid && itemList != null && itemList.isNotEmpty()
        }

}
