package com.tokopedia.flight.filter.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.filter.presentation.FlightFilterSortListener
import com.tokopedia.flight.filter.presentation.adapter.viewholder.FlightFilterDepartureTimeViewHolder
import com.tokopedia.flight.filter.presentation.adapter.viewholder.FlightFilterPriceRangeViewHolder
import com.tokopedia.flight.filter.presentation.adapter.viewholder.FlightFilterTransitViewHolder
import com.tokopedia.flight.filter.presentation.adapter.viewholder.FlightSortViewHolder
import com.tokopedia.flight.filter.presentation.model.DepartureTimeModel
import com.tokopedia.flight.filter.presentation.model.FlightSortModel
import com.tokopedia.flight.filter.presentation.model.PriceRangeModel
import com.tokopedia.flight.filter.presentation.model.TransitModel
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel

/**
 * @author by jessica on 2020-02-20
 */

class FlightFilterSortAdapterTypeFactory(val listener: FlightFilterSortListener, private val initialSortOption: Int, private val filterModel: FlightFilterModel) :
        BaseAdapterTypeFactory() {

    fun type(model: FlightSortModel): Int = TYPE_FLIGHT_SORT

    fun type(model: PriceRangeModel): Int = FlightFilterPriceRangeViewHolder.LAYOUT

    fun type(model: TransitModel): Int = TYPE_FLIGHT_FILTER_TRANSIT

    fun type(model: DepartureTimeModel): Int = TYPE_FLIGHT_FILTER_DEPARTURE_TIME

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            TYPE_FLIGHT_SORT -> FlightSortViewHolder(parent, listener, initialSortOption)
            TYPE_FLIGHT_FILTER_TRANSIT -> FlightFilterTransitViewHolder(parent, listener, filterModel.transitTypeList ?: listOf())
            TYPE_FLIGHT_FILTER_DEPARTURE_TIME -> FlightFilterDepartureTimeViewHolder(parent, listener, filterModel.departureTimeList ?: listOf())
            FlightFilterPriceRangeViewHolder.LAYOUT -> FlightFilterPriceRangeViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    fun getViewHolderLayout(type: Int): Int {
        return when (type) {
            TYPE_FLIGHT_SORT, TYPE_FLIGHT_FILTER_TRANSIT, TYPE_FLIGHT_FILTER_DEPARTURE_TIME -> R.layout.item_flight_filter_sort
            else -> type
        }
    }

    companion object {
        const val TYPE_FLIGHT_SORT = 0
        const val TYPE_FLIGHT_FILTER_TRANSIT = 1
        const val TYPE_FLIGHT_FILTER_DEPARTURE_TIME = 2
    }

}