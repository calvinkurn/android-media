package com.tokopedia.flight.orderlist.data.cache

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.network.CacheUtil
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity
import rx.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author by furqan on 27/04/18.
 */
class FlightOrderDataCacheSource @Inject constructor() {

    val isExpired: Observable<Boolean>
        get() = Observable.just(PersistentCacheManager.instance.isExpired(FLIGHT_DETAIL_CACHE_KEY))

    val cache: Observable<OrderEntity>
        get() {
            val jsonString = PersistentCacheManager.instance.getString(FLIGHT_DETAIL_CACHE_KEY)
            val type = object : TypeToken<OrderEntity>() {

            }.type
            val data = CacheUtil.convertStringToModel<OrderEntity>(jsonString, type)
            return Observable.just(data)
        }

    fun deleteCache(): Observable<Boolean> {
        PersistentCacheManager.instance.delete(FLIGHT_DETAIL_CACHE_KEY)
        return Observable.just(true)
    }

    fun saveCache(orderEntity: OrderEntity) {
        PersistentCacheManager.instance.delete(FLIGHT_DETAIL_CACHE_KEY)

        val type = object : TypeToken<OrderEntity>() {

        }.type

        PersistentCacheManager.instance.put(FLIGHT_DETAIL_CACHE_KEY,
                CacheUtil.convertModelToString(orderEntity, type),
                FLIGHT_DETAIL_CACHE_TIMEOUT)
    }

    companion object {

        private val FLIGHT_DETAIL_CACHE_TIMEOUT = TimeUnit.MINUTES.toSeconds(10)
        private val FLIGHT_DETAIL_CACHE_KEY = "FLIGHT_ORDER_CACHE"
    }
}
