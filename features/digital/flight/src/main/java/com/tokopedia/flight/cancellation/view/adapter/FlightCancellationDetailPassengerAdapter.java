package com.tokopedia.flight.cancellation.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListPassengerViewModel;

/**
 * @author by furqan on 04/05/18.
 */

public class FlightCancellationDetailPassengerAdapter extends BaseListAdapter<FlightCancellationListPassengerViewModel, FlightCancellationDetailPassengerAdapterTypeFactory> {
    public FlightCancellationDetailPassengerAdapter(FlightCancellationDetailPassengerAdapterTypeFactory baseListAdapterTypeFactory) {
        super(baseListAdapterTypeFactory);
    }
}
