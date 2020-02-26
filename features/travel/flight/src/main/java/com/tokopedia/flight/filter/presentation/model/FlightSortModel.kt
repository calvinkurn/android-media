package com.tokopedia.flight.filter.presentation.model

import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.flight.filter.presentation.adapter.FlightFilterSortAdapterTypeFactory

/**
 * @author by jessica on 2020-02-20
 */

class FlightSortModel(
        val selectedOption: Int = TravelSortOption.CHEAPEST,
        title: String = "",
        isSelected: Boolean = false)
    : BaseFilterSortModel(title, isSelected) {
    override fun type(typeFactory: FlightFilterSortAdapterTypeFactory): Int = typeFactory.type(this)
}