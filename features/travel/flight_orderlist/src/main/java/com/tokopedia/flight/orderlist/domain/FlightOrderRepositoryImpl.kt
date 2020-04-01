package com.tokopedia.flight.orderlist.domain

import com.tokopedia.flight.orderlist.data.cloud.FlightOrderDataSource
import com.tokopedia.flight.orderlist.data.cloud.entity.SendEmailEntity
import com.tokopedia.flight.orderlist.domain.model.FlightOrder
import com.tokopedia.flight.orderlist.domain.model.mapper.FlightOrderMapper
import rx.Observable

/**
 * @author by furqan on 03/10/2019
 */
open class FlightOrderRepositoryImpl constructor(
        protected val flightOrderDataSource: FlightOrderDataSource,
        protected val flightOrderMapper: FlightOrderMapper) : FlightOrderRepository {

    override fun getOrders(maps: Map<String, Any>): Observable<List<FlightOrder>> {
        return flightOrderDataSource.getOrders(maps)
                .map { flightOrderMapper.transform(it) }
    }

    override fun getOrder(id: String): Observable<FlightOrder> {
        return flightOrderDataSource.getOrder(id)
                .map { flightOrderMapper.transform(it) }
    }

    override fun sendEmail(params: Map<String, Any>): Observable<SendEmailEntity> {
        return flightOrderDataSource.sendEmail(params)
    }

}