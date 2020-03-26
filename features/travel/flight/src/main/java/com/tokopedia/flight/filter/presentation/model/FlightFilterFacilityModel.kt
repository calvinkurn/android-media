package com.tokopedia.flight.filter.presentation.model

import com.tokopedia.flight.filter.presentation.FlightFilterFacilityEnum
import com.tokopedia.flight.filter.presentation.adapter.FlightFilterSortAdapterTypeFactory

/**
 * @author by furqan on 21/02/2020
 */
class FlightFilterFacilityModel(val facilityEnum: FlightFilterFacilityEnum = FlightFilterFacilityEnum.BAGGAGE,
                                title: String = "",
                                isSelected: Boolean = false)
    : BaseFilterSortModel(title, isSelected) {
    override fun type(typeFactory: FlightFilterSortAdapterTypeFactory): Int = typeFactory.type(this)
}