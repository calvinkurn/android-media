package com.tokopedia.flight.filter.presentation.model

import com.tokopedia.flight.filter.presentation.adapter.FlightFilterSortAdapterTypeFactory
import com.tokopedia.flight.search.presentation.model.filter.DepartureTimeEnum

/**
 * @author by jessica on 2020-02-21
 */

data class DepartureTimeModel(
        val departureTimeEnum: DepartureTimeEnum,
        override var title: String = "",
        override var isSelected: Boolean = false): BaseFilterSortModel() {
    override fun type(typeFactory: FlightFilterSortAdapterTypeFactory): Int = typeFactory.type(this)
}