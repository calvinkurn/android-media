package com.tokopedia.flight.detail.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.detail.view.model.FlightDetailRouteModel

/**
 * Created by furqan on 06/10/21.
 */
class FlightDetailAdapterTypeFactory(private val onFlightDetailListener: OnFlightDetailListener,
                                     private val isShowRefundableTag: Boolean = false)
    : BaseAdapterTypeFactory(), FlightDetailRouteTypeFactory {

    override fun type(viewModel: FlightDetailRouteModel): Int {
        return FlightDetailViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == FlightDetailViewHolder.LAYOUT) {
            FlightDetailViewHolder(parent, onFlightDetailListener, isShowRefundableTag)
        } else {
            super.createViewHolder(parent, type)
        }
    }

    interface OnFlightDetailListener {
        fun getItemCount(): Int
    }
}