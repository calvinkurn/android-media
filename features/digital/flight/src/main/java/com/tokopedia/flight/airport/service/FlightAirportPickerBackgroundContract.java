package com.tokopedia.flight.airport.service;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by zulfikarrahman on 11/21/17.
 */

public interface FlightAirportPickerBackgroundContract {
    interface View extends CustomerView{

        void onGetAirportError(Throwable e);

        void onGetAirport(Boolean isSuccess);
    }

    interface Presenter extends CustomerPresenter<View>{
        void getAirportListCloud(long versionAirport);
    }
}
