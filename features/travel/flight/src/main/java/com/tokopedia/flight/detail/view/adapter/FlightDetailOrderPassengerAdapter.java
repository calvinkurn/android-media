package com.tokopedia.flight.detail.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.flight.detail.view.model.FlightDetailPassenger;

/**
 * Created by alvarisi on 12/22/17.
 */

public class FlightDetailOrderPassengerAdapter extends BaseListAdapter<FlightDetailPassenger, FlightDetailOrderPassengerAdapterTypeFactory> {
    public FlightDetailOrderPassengerAdapter(FlightDetailOrderPassengerAdapterTypeFactory baseListAdapterTypeFactory) {
        super(baseListAdapterTypeFactory);
    }
}
