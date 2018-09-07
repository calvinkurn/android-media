package com.tokopedia.flight.cancellation.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationAttachmentTypeFactory;

/**
 * @author  by alvarisi on 3/26/18.
 */

public class FlightCancellationAttachmentButtonViewModel implements Visitable<FlightCancellationAttachmentTypeFactory> {
    @Override
    public int type(FlightCancellationAttachmentTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
