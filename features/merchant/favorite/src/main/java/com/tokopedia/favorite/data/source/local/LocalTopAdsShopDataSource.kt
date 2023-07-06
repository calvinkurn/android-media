package com.tokopedia.favorite.data.source.local

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.favorite.data.mapper.TopAdsShopMapper
import com.tokopedia.favorite.domain.model.TopAdsShop
import retrofit2.Response

/**
 * @author Kulomady on 2/13/17.
 */
class LocalTopAdsShopDataSource(
        private val context: Context,
        private val gson: Gson
) {

    companion object {
        const val CACHE_KEY_TOP_ADS_SHOP = "TOP_ADS_SHOP"
    }

    fun toAdsShop(): TopAdsShop {
        val data = Response.success(PersistentCacheManager.instance.getString(CACHE_KEY_TOP_ADS_SHOP, null))
        return TopAdsShopMapper(context, gson).call(data)
    }

}
