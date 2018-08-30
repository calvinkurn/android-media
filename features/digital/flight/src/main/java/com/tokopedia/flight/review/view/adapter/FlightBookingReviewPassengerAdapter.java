package com.tokopedia.flight.review.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.flight.review.view.model.FlightDetailPassenger;

/**
 * Created by alvarisi on 12/22/17.
 */

public class FlightBookingReviewPassengerAdapter extends BaseListAdapter<FlightDetailPassenger, FlightBookingReviewPassengerAdapterTypeFactory> {
    public FlightBookingReviewPassengerAdapter(FlightBookingReviewPassengerAdapterTypeFactory baseListAdapterTypeFactory) {
        super(baseListAdapterTypeFactory);
    }
}
