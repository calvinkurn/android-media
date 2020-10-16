package com.tokopedia.browse.homepage.data.source.cache

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.network.CacheUtil
import com.tokopedia.browse.homepage.data.entity.DigitalBrowseMarketplaceData
import com.tokopedia.cachemanager.CacheManager
import rx.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author by furqan on 13/09/18.
 */

class DigitalBrowseServiceCacheSource @Inject
constructor(private val cacheManager: CacheManager) {

    fun saveCache(orderEntity: DigitalBrowseMarketplaceData) {
        cacheManager.delete(DIGITAL_BROWSE_SERVICE_CACHE_KEY)

        val type = object : TypeToken<DigitalBrowseMarketplaceData>() {

        }.type

        cacheManager.put(DIGITAL_BROWSE_SERVICE_CACHE_KEY,
                CacheUtil.convertModelToString(orderEntity, type),
                DIGITAL_BROWSE_SERVICE_CACHE_TIMEOUT)
    }

    fun getCache(): Observable<DigitalBrowseMarketplaceData> {
        var jsonString = cacheManager.getString(DIGITAL_BROWSE_SERVICE_CACHE_KEY)

        if (jsonString != null) {
            val type = object : TypeToken<DigitalBrowseMarketplaceData>() {
            }.type
            val data = CacheUtil.convertStringToModel<DigitalBrowseMarketplaceData>(jsonString, type)
            return Observable.just(data)
        } else {
            return Observable.error(RuntimeException("Cache has expired"))
        }
    }

    companion object {
        private val DIGITAL_BROWSE_SERVICE_CACHE_TIMEOUT = TimeUnit.MINUTES.toSeconds(10)
        private val DIGITAL_BROWSE_SERVICE_CACHE_KEY = "DIGITAL_BROWSE_SERVICE_CACHE_KEY"
    }
}