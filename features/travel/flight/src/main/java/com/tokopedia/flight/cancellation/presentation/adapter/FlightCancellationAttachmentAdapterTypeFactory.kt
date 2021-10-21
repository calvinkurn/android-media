package com.tokopedia.flight.cancellation.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.cancellation.presentation.adapter.viewholder.FlightCancellationAttachmentButtonViewHolder
import com.tokopedia.flight.cancellation.presentation.adapter.viewholder.FlightCancellationAttachmentViewHolder
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationAttachmentButtonModel
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationAttachmentModel
import com.tokopedia.flight.databinding.ItemFlightCancellationAttachmentBinding
import com.tokopedia.flight.databinding.ItemFlightCancellationAttachmentButtonBinding

/**
 * @author by furqan on 20/07/2020
 */
class FlightCancellationAttachmentAdapterTypeFactory(
        private val interactionListener: AdapterInteractionListener,
        private val showChangeButton: Boolean)
    : BaseAdapterTypeFactory(), FlightCancellationAttachmentTypeFactory {

    override fun type(viewModel: FlightCancellationAttachmentModel): Int =
            FlightCancellationAttachmentViewHolder.LAYOUT

    override fun type(viewModel: FlightCancellationAttachmentButtonModel): Int =
            FlightCancellationAttachmentButtonViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> =
            when (type) {
                FlightCancellationAttachmentViewHolder.LAYOUT -> {
                    val binding = ItemFlightCancellationAttachmentBinding.bind(parent)
                    FlightCancellationAttachmentViewHolder(
                        binding,
                        interactionListener,
                        showChangeButton
                    )
                }
                FlightCancellationAttachmentButtonViewHolder.LAYOUT -> {
                    val binding = ItemFlightCancellationAttachmentButtonBinding.bind(parent)
                    FlightCancellationAttachmentButtonViewHolder(binding, interactionListener)
                }
                else -> super.createViewHolder(parent, type)
            }

    interface AdapterInteractionListener {
        fun onUploadAttachmentButtonClicked(position: Int)
        fun viewImage(filePath: String)
    }

}