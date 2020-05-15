package com.tokopedia.flight.dashboard.view.presenter;

import androidx.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.flight.dashboard.view.fragment.model.FlightPassengerModel;

/**
 * Created by alvarisi on 10/26/17.
 */

public interface FlightSelectPassengerView extends CustomerView {
    FlightPassengerModel getCurrentPassengerViewModel();

    void showTotalPassengerErrorMessage(@StringRes int resId);

    void showInfantGreaterThanAdultErrorMessage(@StringRes int resId);

    void showInfantMoreThanFourErrorMessage(@StringRes int resId);

    void showAdultShouldAtleastOneErrorMessage(@StringRes int resId);

    void renderPassengerView(FlightPassengerModel passengerPassData);

    void actionNavigateBack(FlightPassengerModel currentPassengerPassData);
}
