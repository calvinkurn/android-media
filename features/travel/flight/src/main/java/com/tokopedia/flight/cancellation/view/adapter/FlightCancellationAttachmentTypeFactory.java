package com.tokopedia.flight.cancellation.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentButtonViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentViewModel;

/**
 * Created by alvarisi on 3/26/18.
 */

public interface FlightCancellationAttachmentTypeFactory extends AdapterTypeFactory {
    int type(FlightCancellationAttachmentViewModel flightCancellationAttachmentViewModel);

    int type(FlightCancellationAttachmentButtonViewModel flightCancellationAttachmentButtonViewModel);
}
