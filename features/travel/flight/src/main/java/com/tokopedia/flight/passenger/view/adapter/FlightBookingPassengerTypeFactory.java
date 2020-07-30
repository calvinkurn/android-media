package com.tokopedia.flight.passenger.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.passenger.view.model.FlightBookingPassengerModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public interface FlightBookingPassengerTypeFactory {

    int type(FlightBookingPassengerModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
