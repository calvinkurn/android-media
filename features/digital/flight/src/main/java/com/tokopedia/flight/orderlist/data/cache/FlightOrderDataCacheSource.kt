package com.tokopedia.flight.orderlist.data.cache

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.data.model.storage.CacheManager
import com.tokopedia.abstraction.common.utils.network.CacheUtil
import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity

import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

import javax.inject.Inject

import rx.Observable

/**
 * @author by furqan on 27/04/18.
 */

class FlightOrderDataCacheSource @Inject
constructor(private val cacheManager: CacheManager) {

    val isExpired: Observable<Boolean>
        get() = Observable.just(cacheManager.isExpired(FLIGHT_DETAIL_CACHE_KEY))

    val cache: Observable<OrderEntity>
        get() {
            val jsonString = cacheManager.get(FLIGHT_DETAIL_CACHE_KEY)
            val type = object : TypeToken<OrderEntity>() {

            }.type
            val data = CacheUtil.convertStringToModel<OrderEntity>(jsonString, type)
            return Observable.just(data)
        }

    fun deleteCache(): Observable<Boolean> {
        cacheManager.delete(FLIGHT_DETAIL_CACHE_KEY)
        return Observable.just(true)
    }

    fun saveCache(orderEntity: OrderEntity) {
        cacheManager.delete(FLIGHT_DETAIL_CACHE_KEY)

        val type = object : TypeToken<OrderEntity>() {

        }.type

        cacheManager.save(FLIGHT_DETAIL_CACHE_KEY,
                CacheUtil.convertModelToString(orderEntity, type),
                FLIGHT_DETAIL_CACHE_TIMEOUT)
    }

    companion object {

        private val FLIGHT_DETAIL_CACHE_TIMEOUT = TimeUnit.MINUTES.toSeconds(10)
        private val FLIGHT_DETAIL_CACHE_KEY = "FLIGHT_ORDER_CACHE"
    }
}
