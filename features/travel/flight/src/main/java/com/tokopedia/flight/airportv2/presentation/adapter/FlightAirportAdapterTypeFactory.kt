package com.tokopedia.flight.airportv2.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.airport.view.adapter.FlightAirportClickListener
import com.tokopedia.flight.airport.view.adapter.FlightAirportViewHolder
import com.tokopedia.flight.airport.view.adapter.FlightCountryViewHolder
import com.tokopedia.flight.airport.view.model.FlightAirportModel
import com.tokopedia.flight.airport.view.model.FlightCountryAirportModel
import com.tokopedia.flight.common.view.adapter.FlightAdapterTypeFactory

/**
 * @author by furqan on 20/05/2020
 */
class FlightAirportAdapterTypeFactory(private val flightAirportClickListener: FlightAirportClickListener)
    : FlightAdapterTypeFactory() {

    fun type(viewModel: FlightAirportModel): Int = FlightAirportViewHolder.LAYOUT

    fun type(viewModel: FlightCountryAirportModel) = FlightCountryViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> =
            when (type) {
                FlightCountryViewHolder.LAYOUT -> FlightCountryViewHolder(parent)
                FlightAirportViewHolder.LAYOUT -> FlightAirportViewHolder(parent, flightAirportClickListener)
                else -> super.createViewHolder(parent, type)
            }

    override fun onRetryClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}