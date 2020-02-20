package com.tokopedia.flight.filter.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.filter.presentation.FlightFilterSortListener
import com.tokopedia.flight.filter.presentation.adapter.viewholder.FlightFilterPriceRangeViewHolder
import com.tokopedia.flight.filter.presentation.adapter.viewholder.FlightSortViewHolder
import com.tokopedia.flight.filter.presentation.model.FlightSortModel
import com.tokopedia.flight.filter.presentation.model.PriceRangeModel

/**
 * @author by jessica on 2020-02-20
 */

class FlightFilterSortAdapterTypeFactory(val listener: FlightFilterSortListener) :
        BaseAdapterTypeFactory() {

    fun type(model: FlightSortModel): Int = FlightSortViewHolder.LAYOUT

    fun type(model: PriceRangeModel): Int = FlightFilterPriceRangeViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            FlightSortViewHolder.LAYOUT -> FlightSortViewHolder(parent, listener)
            FlightFilterPriceRangeViewHolder.LAYOUT -> FlightFilterPriceRangeViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    companion object {
        const val TYPE_FLIGHT_SORT = 1
    }

}