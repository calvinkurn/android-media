package com.tokopedia.flight.orderlist.data.cloud

import com.tokopedia.flight.common.data.source.cloud.api.FlightApi
import com.tokopedia.flight.orderlist.data.cache.FlightOrderDataCacheSource
import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity
import com.tokopedia.flight.orderlist.data.cloud.entity.SendEmailEntity
import rx.Observable
import javax.inject.Inject

/**
 * Created by alvarisi on 12/6/17.
 */

class FlightOrderDataSource @Inject
constructor(private val flightApi: FlightApi, private val flightOrderDataCacheSource: FlightOrderDataCacheSource) {

    fun getOrders(maps: Map<String, Any>): Observable<List<OrderEntity>> {
        return flightApi.getOrders(maps)
                .map { dataResponseResponse -> dataResponseResponse.body().data }
    }

    fun getOrder(id: String): Observable<OrderEntity> {
        return flightApi.getOrder(id)
                .map { dataResponseResponse -> dataResponseResponse.body().data }
                .flatMap { orderEntity ->
                    flightOrderDataCacheSource.saveCache(orderEntity)
                    return@flatMap Observable.just(orderEntity)
                }
    }

    fun sendEmail(maps: Map<String, Any>): Observable<SendEmailEntity> {
        return flightApi.sendEmail(maps)
                .map { emailResponse -> emailResponse.body() }
    }
}
