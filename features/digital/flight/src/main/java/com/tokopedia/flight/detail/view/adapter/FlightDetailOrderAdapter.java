package com.tokopedia.flight.detail.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;

/**
 * Created by zulfikarrahman on 12/13/17.
 */

public class FlightDetailOrderAdapter extends BaseListAdapter<FlightOrderJourney, FlightDetailOrderTypeFactory> {

    public FlightDetailOrderAdapter(FlightDetailOrderTypeFactory baseListAdapterTypeFactory) {
        super(baseListAdapterTypeFactory);
    }
}
