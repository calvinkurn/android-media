package com.tokopedia.flight.airport.presentation.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.presentation.model.FlightCountryAirportModel

/**
 * Created by zulfikarrahman on 10/24/17.
 */
class FlightCountryViewHolder(itemView: View)
    : AbstractViewHolder<FlightCountryAirportModel>(itemView) {

    private val countryTextView: TextView = itemView.findViewById<View>(R.id.country) as TextView

    override fun bind(country: FlightCountryAirportModel) {
        countryTextView.text = country.countryName.toUpperCase()
    }

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_flight_country
    }

}