package com.tokopedia.flight.cancellation.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.presentation.adapter.FlightCancellationAttachmentAdapterTypeFactory
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationAttachmentButtonModel
import kotlinx.android.synthetic.main.item_flight_cancellation_attachment_button.view.*

/**
 * @author by furqan on 20/07/2020
 */
class FlightCancellationAttachmentButtonViewHolder(itemView: View,
                                                   private val listener: FlightCancellationAttachmentAdapterTypeFactory.AdapterInteractionListener)
    : AbstractViewHolder<FlightCancellationAttachmentButtonModel>(itemView) {

    override fun bind(element: FlightCancellationAttachmentButtonModel?) {
        with(itemView) {
            upload_container.setOnClickListener {
                listener.onUploadAttachmentButtonClicked(adapterPosition)
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_flight_cancellation_attachment_button
    }
}