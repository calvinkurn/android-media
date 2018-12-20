package com.tokopedia.browse.homepage.data.source.cache

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.data.model.storage.CacheManager
import com.tokopedia.abstraction.common.utils.network.CacheUtil
import com.tokopedia.browse.homepage.data.entity.DigitalBrowseMarketplaceData
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

        cacheManager.save(DIGITAL_BROWSE_SERVICE_CACHE_KEY,
                CacheUtil.convertModelToString(orderEntity, type),
                DIGITAL_BROWSE_SERVICE_CACHE_TIMEOUT)
    }

    fun getCache(): Observable<DigitalBrowseMarketplaceData> {
        val jsonString = cacheManager.get(DIGITAL_BROWSE_SERVICE_CACHE_KEY)

        val type = object : TypeToken<DigitalBrowseMarketplaceData>() {

        }.type
        val data = CacheUtil.convertStringToModel<DigitalBrowseMarketplaceData>(jsonString, type)
        return Observable.just(data)
    }

    companion object {
        private val DIGITAL_BROWSE_SERVICE_CACHE_TIMEOUT = TimeUnit.DAYS.toSeconds(30)
        private val DIGITAL_BROWSE_SERVICE_CACHE_KEY = "DIGITAL_BROWSE_SERVICE_CACHE_KEY"
    }
}