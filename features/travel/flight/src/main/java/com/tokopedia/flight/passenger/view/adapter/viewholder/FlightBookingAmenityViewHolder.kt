package com.tokopedia.flight.passenger.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.databinding.ItemFlightBookingAmenityBinding
import com.tokopedia.flight.passenger.view.model.FlightBookingAmenityModel
import com.tokopedia.unifyprinciples.R

/**
 * Created by furqan on 06/10/21.
 */
class FlightBookingAmenityViewHolder(val binding: ItemFlightBookingAmenityBinding,
                                     private val listenerCheckedLuggage: ListenerCheckedLuggage?)
    : AbstractViewHolder<FlightBookingAmenityModel>(binding.root) {


    override fun bind(flightBookingLuggageViewModel: FlightBookingAmenityModel) {
        var isItemChecked = false
        listenerCheckedLuggage?.let {
            isItemChecked = it.isItemChecked(flightBookingLuggageViewModel)
        }
        binding.tvTitle.text = String.format("%s - %s", flightBookingLuggageViewModel.title, flightBookingLuggageViewModel.price)
        if (isItemChecked) {
            binding.imageChecked.visibility = View.VISIBLE
            binding.tvTitle.setTextColor(ContextCompat.getColor(itemView.context, R.color.Unify_GN500))
        } else {
            binding.imageChecked.visibility = View.INVISIBLE
            binding.tvTitle.setTextColor(ContextCompat.getColor(itemView.context, R.color.Unify_NN950_68))
        }
    }

    interface ListenerCheckedLuggage {
        fun isItemChecked(selectedItem: FlightBookingAmenityModel?): Boolean
        fun resetItemCheck()
    }

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = com.tokopedia.flight.R.layout.item_flight_booking_amenity
    }

}