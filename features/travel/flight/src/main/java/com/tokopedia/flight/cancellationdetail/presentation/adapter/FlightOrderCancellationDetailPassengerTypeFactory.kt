package com.tokopedia.flight.cancellationdetail.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.flight.cancellationdetail.presentation.model.FlightOrderCancellationDetailPassengerModel

/**
 * @author by furqan on 08/01/2021
 */
interface FlightOrderCancellationDetailPassengerTypeFactory : AdapterTypeFactory {
    fun type(model: FlightOrderCancellationDetailPassengerModel): Int
}