package com.tokopedia.flight.cancellation.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.cancellation.presentation.adapter.viewholder.FlightCancellationReviewViewHolder
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationModel
import com.tokopedia.flight.databinding.ItemFlightCancellationReviewBinding

/**
 * @author by furqan on 21/07/2020
 */
class FlightCancellationReviewAdapterTypeFactory : BaseAdapterTypeFactory(), FlightCancellationTypeFactory {
    override fun type(cancellationModel: FlightCancellationModel): Int =
            FlightCancellationReviewViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> =
            when (type) {
                FlightCancellationReviewViewHolder.LAYOUT -> {
                    val binding = ItemFlightCancellationReviewBinding.bind(parent)
                    FlightCancellationReviewViewHolder(binding)
                }
                else -> super.createViewHolder(parent, type)
            }

}