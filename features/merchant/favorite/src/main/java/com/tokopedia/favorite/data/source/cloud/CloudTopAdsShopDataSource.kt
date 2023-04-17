package com.tokopedia.favorite.data.source.cloud

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.favorite.data.mapper.TopAdsShopMapper
import com.tokopedia.favorite.data.source.apis.service.TopAdsService
import com.tokopedia.favorite.data.source.local.LocalTopAdsShopDataSource
import com.tokopedia.favorite.domain.model.TopAdsShop
import com.tokopedia.favorite.utils.HttpResponseValidator
import retrofit2.Response
import java.util.*

/**
 * @author Kulomady on 1/19/17.
 */
class CloudTopAdsShopDataSource(
        private val context: Context,
        private val gson: Gson,
        private val topAdsService: TopAdsService
) {
    suspend fun suspendGetTopAdsShop(param: HashMap<String, Any>): TopAdsShop {
        val response = topAdsService.suspendGetShopTopAds(param)
        HttpResponseValidator.validate(object : HttpResponseValidator.HttpValidationListener {
            override fun onPassValidation(response: Response<String?>?) {
                saveResponseToCache(response)
            }
        }).call(response)
        return TopAdsShopMapper(context, gson).call(response)
    }


    private fun saveResponseToCache(stringResponse: Response<String?>?) {
        PersistentCacheManager.instance.put(
                LocalTopAdsShopDataSource.CACHE_KEY_TOP_ADS_SHOP,
                stringResponse?.body(),
                -System.currentTimeMillis()
        )
    }

}
