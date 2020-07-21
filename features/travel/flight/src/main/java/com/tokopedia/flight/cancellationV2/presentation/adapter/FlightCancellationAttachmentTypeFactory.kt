package com.tokopedia.flight.cancellationV2.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.flight.cancellationV2.presentation.model.FlightCancellationAttachmentButtonModel
import com.tokopedia.flight.cancellationV2.presentation.model.FlightCancellationAttachmentModel

/**
 * @author by furqan on 20/07/2020
 */
interface FlightCancellationAttachmentTypeFactory : AdapterTypeFactory {
    fun type(viewModel: FlightCancellationAttachmentModel): Int
    fun type(viewModel: FlightCancellationAttachmentButtonModel): Int
}