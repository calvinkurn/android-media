package com.tokopedia.flight.cancellationV2.data

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.network.CacheUtil
import com.tokopedia.cachemanager.PersistentCacheManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author by furqan on 15/07/2020
 */
class FlightCancellationReasonDataCacheSource @Inject constructor() {

    fun saveCache(reasonList: List<FlightCancellationPassengerEntity.Reason>) {
        PersistentCacheManager.instance.delete(FLIGHT_CANCELLATION_REASON_CACHE_KEY)
        val type = object : TypeToken<List<FlightCancellationPassengerEntity.Reason>>() {}.type
        val jsonString = CacheUtil.convertListModelToString(reasonList, type)
        PersistentCacheManager.instance.put(FLIGHT_CANCELLATION_REASON_CACHE_KEY,
                jsonString,
                FLIGHT_CANCELLATION_REASON_CACHE_TIMEOUT)
    }

    fun getCache(): List<FlightCancellationPassengerEntity.Reason> {
        val jsonString = PersistentCacheManager.instance.getString(FLIGHT_CANCELLATION_REASON_CACHE_KEY)
        val type = object : TypeToken<List<FlightCancellationPassengerEntity.Reason>>() {}.type
        return CacheUtil.convertStringToListModel(jsonString, type)
    }

    companion object {
        private val FLIGHT_CANCELLATION_REASON_CACHE_TIMEOUT = TimeUnit.DAYS.toSeconds(10)
        private const val FLIGHT_CANCELLATION_REASON_CACHE_KEY = "FLIGHT_CANCELLATION_REASON_CACHE_KEY"
    }
}