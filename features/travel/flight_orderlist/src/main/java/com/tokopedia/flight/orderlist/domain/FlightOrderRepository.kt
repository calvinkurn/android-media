package com.tokopedia.flight.orderlist.domain

import com.tokopedia.flight.orderlist.data.cloud.entity.SendEmailEntity
import com.tokopedia.flight.orderlist.domain.model.FlightOrder
import rx.Observable

/**
 * @author by furqan on 03/10/2019
 */
interface FlightOrderRepository {

    fun getOrders(maps: Map<String, Any>): Observable<List<FlightOrder>>

    fun getOrder(id: String): Observable<FlightOrder>

    fun sendEmail(params: Map<String, Any>): Observable<SendEmailEntity>

}