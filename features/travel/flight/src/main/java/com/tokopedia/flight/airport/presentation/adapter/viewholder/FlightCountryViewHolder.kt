package com.tokopedia.flight.airport.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.presentation.model.FlightCountryAirportModel
import com.tokopedia.flight.databinding.ItemFlightCountryBinding

/**
 * Created by zulfikarrahman on 10/24/17.
 */
class FlightCountryViewHolder(val binding: ItemFlightCountryBinding)
    : AbstractViewHolder<FlightCountryAirportModel>(binding.root) {

    override fun bind(country: FlightCountryAirportModel) {
        binding.country.text = country.countryName.toUpperCase()
    }

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_flight_country
    }

}