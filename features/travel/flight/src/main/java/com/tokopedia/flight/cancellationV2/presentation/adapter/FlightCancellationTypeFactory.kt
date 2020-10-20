package com.tokopedia.flight.cancellationV2.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.flight.cancellationV2.presentation.model.FlightCancellationModel

/**
 * @author by furqan on 14/07/2020
 */
interface FlightCancellationTypeFactory : AdapterTypeFactory {
    fun type(cancellationModel: FlightCancellationModel): Int
}