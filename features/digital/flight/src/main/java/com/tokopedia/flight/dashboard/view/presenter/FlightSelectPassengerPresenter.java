package com.tokopedia.flight.dashboard.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by alvarisi on 10/26/17.
 */

public interface FlightSelectPassengerPresenter extends CustomerPresenter<FlightSelectPassengerView>{
    void onAdultPassengerCountChange(int number);

    void onChildrenPassengerCountChange(int number);

    void onInfantPassengerCountChange(int number);

    void initialize();

    void onSaveButtonClicked();

}
