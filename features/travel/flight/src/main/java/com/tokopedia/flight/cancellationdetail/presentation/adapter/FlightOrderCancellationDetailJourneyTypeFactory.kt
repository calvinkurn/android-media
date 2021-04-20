package com.tokopedia.flight.cancellationdetail.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailJourneyModel

/**
 * @author by furqan on 07/01/2021
 */
interface FlightOrderCancellationDetailJourneyTypeFactory : AdapterTypeFactory {
    fun type(model: FlightOrderDetailJourneyModel): Int
}