package com.tokopedia.flight.searchV4.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.searchV4.presentation.adapter.viewholder.FlightSearchAdapterTypeFactory

/**
 * @author by furqan on 20/04/2020
 */
class FlightSearchSeeAllResultModel(val newPrice: String = "",
                                    val isOnlyBestPairing: Boolean = false)
    : Visitable<FlightSearchAdapterTypeFactory> {
    override fun type(typeFactory: FlightSearchAdapterTypeFactory): Int = typeFactory.type(this)
}