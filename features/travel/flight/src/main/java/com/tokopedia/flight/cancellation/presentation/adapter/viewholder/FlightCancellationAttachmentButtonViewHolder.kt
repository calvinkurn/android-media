package com.tokopedia.flight.cancellation.presentation.adapter.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.presentation.adapter.FlightCancellationAttachmentAdapterTypeFactory
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationAttachmentButtonModel
import com.tokopedia.flight.databinding.ItemFlightCancellationAttachmentButtonBinding

/**
 * @author by furqan on 20/07/2020
 */
class FlightCancellationAttachmentButtonViewHolder(val binding: ItemFlightCancellationAttachmentButtonBinding,
                                                   private val listener: FlightCancellationAttachmentAdapterTypeFactory.AdapterInteractionListener)
    : AbstractViewHolder<FlightCancellationAttachmentButtonModel>(binding.root) {

    override fun bind(element: FlightCancellationAttachmentButtonModel?) {
        with(binding) {
            uploadContainer.setOnClickListener {
                listener.onUploadAttachmentButtonClicked(adapterPosition)
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_flight_cancellation_attachment_button
    }
}