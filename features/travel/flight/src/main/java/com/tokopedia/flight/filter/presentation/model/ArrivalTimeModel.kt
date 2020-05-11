package com.tokopedia.flight.filter.presentation.model

import com.tokopedia.flight.filter.presentation.adapter.FlightFilterSortAdapterTypeFactory
import com.tokopedia.flight.search.presentation.model.filter.DepartureTimeEnum

/**
 * @author by jessica on 2020-02-21
 */

class ArrivalTimeModel(
        val arrivalTimeEnum: DepartureTimeEnum,
        title: String = "",
        isSelected: Boolean = false)
    : BaseFilterSortModel(title, isSelected) {
    override fun type(typeFactory: FlightFilterSortAdapterTypeFactory): Int = typeFactory.type(this)
}