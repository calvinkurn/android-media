package com.tokopedia.flight.detail.view.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.flight.detail.view.model.FlightDetailRouteModel

/**
 * Created by furqan on 06/10/21.
 */
interface FlightDetailRouteTypeFactory : AdapterTypeFactory {
    fun type(viewModel: FlightDetailRouteModel): Int
}