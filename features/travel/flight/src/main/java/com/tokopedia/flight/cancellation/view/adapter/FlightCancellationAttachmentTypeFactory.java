package com.tokopedia.flight.cancellation.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentButtonModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentModel;

/**
 * Created by alvarisi on 3/26/18.
 */

public interface FlightCancellationAttachmentTypeFactory extends AdapterTypeFactory {
    int type(FlightCancellationAttachmentModel flightCancellationAttachmentViewModel);

    int type(FlightCancellationAttachmentButtonModel flightCancellationAttachmentButtonViewModel);
}
