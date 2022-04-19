package com.tokopedia.flight.cancellation.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationAttachmentButtonModel
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationAttachmentModel

/**
 * @author by furqan on 20/07/2020
 */
interface FlightCancellationAttachmentTypeFactory : AdapterTypeFactory {
    fun type(viewModel: FlightCancellationAttachmentModel): Int
    fun type(viewModel: FlightCancellationAttachmentButtonModel): Int
}