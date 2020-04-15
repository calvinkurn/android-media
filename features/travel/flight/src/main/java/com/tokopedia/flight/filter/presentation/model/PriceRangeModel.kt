package com.tokopedia.flight.filter.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.filter.presentation.adapter.FlightFilterSortAdapterTypeFactory

/**
 * @author by furqan on 20/02/2020
 */
data class PriceRangeModel(val initialStartValue: Int = 0,
                           val initialEndValue: Int = 0,
                           var selectedStartValue: Int = 0,
                           var selectedEndValue: Int = 0)
    : BaseFilterSortModel(), Visitable<FlightFilterSortAdapterTypeFactory> {

    override fun type(typeFactory: FlightFilterSortAdapterTypeFactory): Int = typeFactory.type(this)

}