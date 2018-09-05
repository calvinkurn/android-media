package com.tokopedia.flight.dashboard.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;
import com.tokopedia.flight.dashboard.view.validator.FlightSelectPassengerValidator;

import javax.inject.Inject;

/**
 * Created by alvarisi on 10/26/17.
 */

public class FlightSelectPassengerPresenterImpl extends BaseDaggerPresenter<FlightSelectPassengerView> implements FlightSelectPassengerPresenter {
    private FlightSelectPassengerValidator validator;

    @Inject
    public FlightSelectPassengerPresenterImpl(FlightSelectPassengerValidator validator) {
        this.validator = validator;
    }

    @Override
    public void onAdultPassengerCountChange(int number) {
        FlightPassengerViewModel passengerViewModel = clonePassData(getView().getCurrentPassengerViewModel());
        passengerViewModel.setAdult(number);
        if (validatePassenger(passengerViewModel)) {
            getView().renderPassengerView(passengerViewModel);
        }
        else
            getView().renderPassengerView(getView().getCurrentPassengerViewModel());
    }

    private FlightPassengerViewModel clonePassData(FlightPassengerViewModel passengerViewModel) {
        FlightPassengerViewModel flightPassengerViewModel = null;
        try {
            flightPassengerViewModel = (FlightPassengerViewModel) passengerViewModel.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException("CloneNotSupportedException FlightPassengerViewModel");
        }
        return flightPassengerViewModel;
    }

    private boolean validatePassenger(FlightPassengerViewModel passengerPassData) {
        boolean isValid = true;
        if (!validator.validateTotalPassenger(passengerPassData)) {
            isValid = false;
            getView().showTotalPassengerErrorMessage(R.string.select_passenger_total_passenger_error_message);
        } else if (!validator.validateInfantNotGreaterThanAdult(passengerPassData)) {
            isValid = false;
            getView().showInfantGreaterThanAdultErrorMessage(R.string.select_passenger_infant_greater_than_adult_error_message);
        } else if (!validator.validateAdultCountAtleastOne(passengerPassData)) {
            isValid = false;
            getView().showAdultShouldAtleastOneErrorMessage(R.string.select_passenger_adult_atleast_one_error_message);
        }
        return isValid;
    }

    @Override
    public void onChildrenPassengerCountChange(int number) {
        FlightPassengerViewModel passengerPassData = clonePassData(getView().getCurrentPassengerViewModel());
        passengerPassData.setChildren(number);
        if (validatePassenger(passengerPassData))
            getView().renderPassengerView(passengerPassData);
        else
            getView().renderPassengerView(getView().getCurrentPassengerViewModel());
    }

    @Override
    public void onInfantPassengerCountChange(int number) {
        FlightPassengerViewModel passengerPassData = clonePassData(getView().getCurrentPassengerViewModel());
        passengerPassData.setInfant(number);
        if (validatePassenger(passengerPassData))
            getView().renderPassengerView(passengerPassData);
        else
            getView().renderPassengerView(getView().getCurrentPassengerViewModel());
    }

    @Override
    public void initialize() {
        getView().renderPassengerView(getView().getCurrentPassengerViewModel());
    }

    @Override
    public void onSaveButtonClicked() {
        FlightPassengerViewModel passengerPassData = clonePassData(getView().getCurrentPassengerViewModel());
        if (validatePassenger(passengerPassData)) {
            getView().actionNavigateBack(passengerPassData);
        }
    }
}
