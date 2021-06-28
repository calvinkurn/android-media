package com.tokopedia.flight.detail.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.detail.view.adapter.viewholder.FlightDetailFacilityViewHolder
import com.tokopedia.flight.detail.view.model.FlightDetailRouteModel

/**
 * Created by furqan on 06/10/21.
 */
class FlightDetailFacilityAdapterTypeFactory : BaseAdapterTypeFactory(), FlightDetailRouteTypeFactory {

    override fun type(viewModel: FlightDetailRouteModel): Int {
        return FlightDetailFacilityViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == FlightDetailFacilityViewHolder.LAYOUT) {
            FlightDetailFacilityViewHolder(parent)
        } else {
            super.createViewHolder(parent, type)
        }
    }

}