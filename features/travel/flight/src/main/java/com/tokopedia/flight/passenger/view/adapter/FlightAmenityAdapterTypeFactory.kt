package com.tokopedia.flight.passenger.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.passenger.view.adapter.viewholder.FlightBookingAmenityViewHolder
import com.tokopedia.flight.passenger.view.model.FlightBookingAmenityModel

/**
 * Created by furqan on 06/10/21.
 */
class FlightAmenityAdapterTypeFactory(private val listenerCheckedClass: FlightBookingAmenityViewHolder.ListenerCheckedLuggage)
    : BaseAdapterTypeFactory() {

    fun type(viewModel: FlightBookingAmenityModel): Int {
        return FlightBookingAmenityViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == FlightBookingAmenityViewHolder.LAYOUT) {
            FlightBookingAmenityViewHolder(parent, listenerCheckedClass)
        } else {
            super.createViewHolder(parent, type)
        }
    }
}