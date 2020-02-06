package com.tokopedia.flight.bookingV2.presentation.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.bookingV2.presentation.viewmodel.FlightBookingPassengerViewModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public interface FlightBookingPassengerTypeFactory {

    int type(FlightBookingPassengerViewModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
