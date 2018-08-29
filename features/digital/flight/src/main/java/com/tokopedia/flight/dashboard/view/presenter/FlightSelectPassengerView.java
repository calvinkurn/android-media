package com.tokopedia.flight.dashboard.view.presenter;

import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;

/**
 * Created by alvarisi on 10/26/17.
 */

public interface FlightSelectPassengerView extends CustomerView {
    FlightPassengerViewModel getCurrentPassengerViewModel();

    void showTotalPassengerErrorMessage(@StringRes int resId);

    void showInfantGreaterThanAdultErrorMessage(@StringRes int resId);

    void showAdultShouldAtleastOneErrorMessage(@StringRes int resId);

    void renderPassengerView(FlightPassengerViewModel passengerPassData);

    void actionNavigateBack(FlightPassengerViewModel currentPassengerPassData);
}
