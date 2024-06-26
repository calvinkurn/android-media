package com.tokopedia.flight.airport.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.airport.presentation.adapter.viewholder.FlightAirportClickListener
import com.tokopedia.flight.airport.presentation.adapter.viewholder.FlightAirportViewHolder
import com.tokopedia.flight.airport.presentation.adapter.viewholder.FlightCountryViewHolder
import com.tokopedia.flight.airport.presentation.model.FlightAirportModel
import com.tokopedia.flight.airport.presentation.model.FlightCountryAirportModel
import com.tokopedia.flight.common.view.adapter.FlightAdapterTypeFactory
import com.tokopedia.flight.databinding.ItemFlightAirportBinding
import com.tokopedia.flight.databinding.ItemFlightCountryBinding

/**
 * @author by furqan on 20/05/2020
 */
class FlightAirportAdapterTypeFactory(private val flightAirportClickListener: FlightAirportClickListener)
    : FlightAdapterTypeFactory() {

    fun type(viewModel: FlightAirportModel): Int = FlightAirportViewHolder.LAYOUT

    fun type(viewModel: FlightCountryAirportModel) = FlightCountryViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> =
            when (type) {
                FlightCountryViewHolder.LAYOUT -> {
                    val binding = ItemFlightCountryBinding.bind(parent)
                    FlightCountryViewHolder(binding)
                }
                FlightAirportViewHolder.LAYOUT -> {
                    val binding = ItemFlightAirportBinding.bind(parent)
                    FlightAirportViewHolder(binding, flightAirportClickListener)
                }
                else -> super.createViewHolder(parent, type)
            }

    override fun onRetryClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}