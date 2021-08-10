package com.tokopedia.flight.passenger.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.passenger.view.model.FlightBookingAmenityModel
import com.tokopedia.unifyprinciples.R

/**
 * Created by furqan on 06/10/21.
 */
class FlightBookingAmenityViewHolder(itemView: View,
                                     private val listenerCheckedLuggage: ListenerCheckedLuggage?)
    : AbstractViewHolder<FlightBookingAmenityModel>(itemView) {

    private val title: TextView = itemView.findViewById<View>(com.tokopedia.flight.R.id.tv_title) as TextView
    private val imageChecked: ImageView = itemView.findViewById<View>(com.tokopedia.flight.R.id.image_checked) as ImageView

    override fun bind(flightBookingLuggageViewModel: FlightBookingAmenityModel) {
        var isItemChecked = false
        listenerCheckedLuggage?.let {
            isItemChecked = it.isItemChecked(flightBookingLuggageViewModel)
        }
        title.text = String.format("%s - %s", flightBookingLuggageViewModel.title, flightBookingLuggageViewModel.price)
        if (isItemChecked) {
            imageChecked.visibility = View.VISIBLE
            title.setTextColor(ContextCompat.getColor(itemView.context, R.color.Unify_G500))
        } else {
            imageChecked.visibility = View.INVISIBLE
            title.setTextColor(ContextCompat.getColor(itemView.context, R.color.Unify_N700_68))
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