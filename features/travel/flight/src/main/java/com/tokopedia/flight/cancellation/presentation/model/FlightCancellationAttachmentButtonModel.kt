package com.tokopedia.flight.cancellation.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.cancellation.presentation.adapter.FlightCancellationAttachmentTypeFactory

/**
 * @author by furqan on 20/07/2020
 */
class FlightCancellationAttachmentButtonModel()
    : Visitable<FlightCancellationAttachmentTypeFactory> {
    override fun type(typeFactory: FlightCancellationAttachmentTypeFactory): Int =
            typeFactory.type(this)
}