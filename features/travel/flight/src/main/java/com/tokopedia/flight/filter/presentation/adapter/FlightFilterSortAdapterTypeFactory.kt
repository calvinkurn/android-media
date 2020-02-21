package com.tokopedia.flight.filter.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.filter.presentation.FlightFilterSortListener
import com.tokopedia.flight.filter.presentation.adapter.viewholder.FlightFilterPriceRangeViewHolder
import com.tokopedia.flight.filter.presentation.adapter.viewholder.FlightFilterTransitViewHolder
import com.tokopedia.flight.filter.presentation.adapter.viewholder.FlightSortViewHolder
import com.tokopedia.flight.filter.presentation.model.FlightSortModel
import com.tokopedia.flight.filter.presentation.model.PriceRangeModel
import com.tokopedia.flight.filter.presentation.model.TransitModel
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.search.presentation.model.filter.TransitEnum

/**
 * @author by jessica on 2020-02-20
 */

class FlightFilterSortAdapterTypeFactory(val listener: FlightFilterSortListener, private val initialSortOption: Int, private val filterModel: FlightFilterModel) :
        BaseAdapterTypeFactory() {

    fun type(model: FlightSortModel): Int = TYPE_FLIGHT_SORT

    fun type(model: PriceRangeModel): Int = FlightFilterPriceRangeViewHolder.LAYOUT

    fun type(model: TransitModel): Int = TYPE_FLIGHT_TRANSIT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            TYPE_FLIGHT_SORT -> FlightSortViewHolder(parent, listener, initialSortOption)
            TYPE_FLIGHT_TRANSIT -> FlightFilterTransitViewHolder(parent, listener, filterModel.transitTypeList)
            FlightFilterPriceRangeViewHolder.LAYOUT -> FlightFilterPriceRangeViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    companion object {
        const val TYPE_FLIGHT_SORT = 0
        const val TYPE_FLIGHT_TRANSIT = 1
    }

}