package com.tokopedia.flight.cancellationdetail.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.cancellationdetail.presentation.adapter.viewholder.FlightOrderCancellationDetailJourneyViewHolder
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailJourneyModel

/**
 * @author by furqan on 07/01/2021
 */
class FlightOrderCancellationDetailJourneyAdapterJourneyTypeFactory(private val listener: FlightOrderCancellationDetailJourneyViewHolder.Listener,
                                                                    private val titleFontSize: Float)
    : BaseAdapterTypeFactory(), FlightOrderCancellationDetailJourneyTypeFactory {

    override fun type(model: FlightOrderDetailJourneyModel): Int =
            FlightOrderCancellationDetailJourneyViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> =
            when (type) {
                FlightOrderCancellationDetailJourneyViewHolder.LAYOUT -> FlightOrderCancellationDetailJourneyViewHolder(listener, titleFontSize, parent)
                else -> super.createViewHolder(parent, type)
            }
}