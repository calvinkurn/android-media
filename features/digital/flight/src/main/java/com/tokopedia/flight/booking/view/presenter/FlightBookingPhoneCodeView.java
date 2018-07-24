package com.tokopedia.flight.booking.view.presenter;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public interface FlightBookingPhoneCodeView extends BaseListViewListener<FlightBookingPhoneCodeViewModel> {
    Activity getActivity();
}
