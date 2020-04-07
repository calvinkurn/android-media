package com.tokopedia.flight.search.data.cache

import com.tokopedia.cachemanager.PersistentCacheManager
import rx.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author by jessica on 06/03/19
 */

open class FlightSearchDataCacheSource @Inject constructor() {

    val isExpired: Observable<Boolean>
        get() = Observable.just(PersistentCacheManager.instance.isExpired(FLIGHT_SPECIAL_TAG_KEY))

    val cache: Observable<String>
        get() {
            val specialTag = PersistentCacheManager.instance.getString(FLIGHT_SPECIAL_TAG_KEY)
            return Observable.just(specialTag)
        }

    val cacheCoroutine: String
        get() {
            return PersistentCacheManager.instance.getString(FLIGHT_SPECIAL_TAG_KEY)?:""
        }

    fun deleteCache(): Observable<Boolean> {
        PersistentCacheManager.instance.delete(FLIGHT_SPECIAL_TAG_KEY)
        return Observable.just(true)
    }

    fun saveCache(specialTag: String) {
        PersistentCacheManager.instance.delete(FLIGHT_SPECIAL_TAG_KEY)

        PersistentCacheManager.instance.put(FLIGHT_SPECIAL_TAG_KEY,
                specialTag,
                FLIGHT_SPECIAL_TAG_TIMEOUT)
    }

    companion object {
        private val FLIGHT_SPECIAL_TAG_TIMEOUT = TimeUnit.HOURS.toSeconds(1)
        private val FLIGHT_SPECIAL_TAG_KEY = "FLIGHT_SPECIAL_TAG"
    }
}