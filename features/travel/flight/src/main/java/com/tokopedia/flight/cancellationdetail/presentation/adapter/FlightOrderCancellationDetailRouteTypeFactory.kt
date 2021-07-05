package com.tokopedia.flight.cancellationdetail.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailRouteModel

/**
 * @author by furqan on 07/01/2021
 */
interface FlightOrderCancellationDetailRouteTypeFactory : AdapterTypeFactory {
    fun type(model: FlightOrderDetailRouteModel): Int
}