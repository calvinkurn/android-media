package com.tokopedia.flight.filter.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder.CheckableInteractionListener
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.filter.presentation.adapter.viewholder.FlightFilterAirlineViewHolder
import com.tokopedia.flight.search.presentation.model.statistics.AirlineStat

/**
 * Created by alvarisi on 12/21/17.
 */
class FlightFilterAirlineAdapterTypeFactory(private val interactionListener: CheckableInteractionListener)
    : BaseAdapterTypeFactory(),
        BaseListCheckableTypeFactory<AirlineStat> {

    override fun type(viewModel: AirlineStat): Int = FlightFilterAirlineViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> =
            if (type == FlightFilterAirlineViewHolder.LAYOUT) {
                FlightFilterAirlineViewHolder(parent, interactionListener)
            } else {
                super.createViewHolder(parent, type)
            }
}