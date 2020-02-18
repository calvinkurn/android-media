package com.tokopedia.flight.filter.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter

/**
 * @author by furqan on 18/02/2020
 */
class FlightFilterAirlineAdapter(adapterTypeFactory: FlightFilterAirlineAdapterTypeFactory, data: List<Visitable<*>>)
    : BaseAdapter<FlightFilterAirlineAdapterTypeFactory>(adapterTypeFactory, data)