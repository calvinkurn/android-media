package com.tokopedia.flight.booking.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public interface FlightBookingPhoneCodePresenter extends CustomerPresenter<FlightBookingPhoneCodeView> {
    void getPhoneCodeList();

    void getPhoneCodeList(String text);

    void onDestroyView();
}
