package com.tokopedia.flight.cancellationdetail.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.flight.cancellationdetail.presentation.model.FlightOrderCancellationListModel

/**
 * @author by furqan on 06/01/2021
 */
interface FlightOrderCancellationListTypeFactory : AdapterTypeFactory {
    fun type(model: FlightOrderCancellationListModel): Int
}