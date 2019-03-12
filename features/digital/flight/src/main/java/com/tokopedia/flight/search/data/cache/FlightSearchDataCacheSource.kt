package com.tokopedia.flight.search.data.cache

import com.tokopedia.abstraction.common.data.model.storage.CacheManager
import rx.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author by jessica on 06/03/19
 */

class FlightSearchDataCacheSource @Inject
constructor(private val cacheManager: CacheManager) {

    val isExpired: Observable<Boolean>
        get() = Observable.just(cacheManager.isExpired(FLIGHT_SPECIAL_TAG_KEY))

    val cache: Observable<String>
        get() {
            val specialTag = cacheManager.get(FLIGHT_SPECIAL_TAG_KEY)
            return Observable.just(specialTag)
        }

    fun deleteCache(): Observable<Boolean> {
        cacheManager.delete(FLIGHT_SPECIAL_TAG_KEY)
        return Observable.just(true)
    }

    fun saveCache(specialTag: String) {
        cacheManager.delete(FLIGHT_SPECIAL_TAG_KEY)

        cacheManager.save(FLIGHT_SPECIAL_TAG_KEY,
                specialTag,
                FLIGHT_SPECIAL_TAG_TIMEOUT)
    }

    companion object {
        private val FLIGHT_SPECIAL_TAG_TIMEOUT = TimeUnit.HOURS.toSeconds(1)
        private val FLIGHT_SPECIAL_TAG_KEY = "FLIGHT_SPECIAL_TAG"
    }
}