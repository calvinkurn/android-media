package com.tokopedia.flight.cancellation.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationModel

/**
 * @author by furqan on 14/07/2020
 */
interface FlightCancellationTypeFactory : AdapterTypeFactory {
    fun type(cancellationModel: FlightCancellationModel): Int
}