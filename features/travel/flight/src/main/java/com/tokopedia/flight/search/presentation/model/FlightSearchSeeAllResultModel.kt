package com.tokopedia.flight.search.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.search.presentation.adapter.viewholder.FlightSearchAdapterTypeFactory

/**
 * @author by furqan on 20/04/2020
 */
class FlightSearchSeeAllResultModel(val newPrice: String = "",
                                    val isOnlyBestPairing: Boolean = false)
    : Visitable<FlightSearchAdapterTypeFactory> {
    override fun type(typeFactory: FlightSearchAdapterTypeFactory): Int = typeFactory.type(this)
}