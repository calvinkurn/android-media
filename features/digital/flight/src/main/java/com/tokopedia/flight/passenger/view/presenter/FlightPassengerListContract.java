package com.tokopedia.flight.passenger.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;

import java.util.List;

/**
 * @author by furqan on 26/02/18.
 */

public interface FlightPassengerListContract {
    interface View extends CustomerView {
        List<FlightBookingPassengerViewModel> getPassengerViewModelList();

        void setPassengerViewModelList(List<FlightBookingPassengerViewModel> passengerViewModelList);

        void renderPassengerList();

        FlightBookingPassengerViewModel getCurrentPassenger();

        void setCurrentPassanger(FlightBookingPassengerViewModel selectedPassenger);

        String getSalutationString(int resId);

        void onSelectPassengerSuccess(FlightBookingPassengerViewModel selectedPassenger);

        String getSelectedPassengerId();

        String getString(int resId);

        String getRequestId();

        void onGetListError(Throwable throwable);

        String getDepartureDate();

        void showSuccessChangePassengerData();

        void onSuccessDeletePassengerData();

        void stopTrace();

    }

    interface Presenter {
        void onViewCreated();

        boolean isPassengerSame(FlightBookingPassengerViewModel selectedPassenger);

        void selectPassenger(FlightBookingPassengerViewModel selectedPassenger);

        void onDestroyView();

        void deletePassenger(String passengerId);

        void onSuccessUpdatePassengerData();

        void getSavedPassengerList();
    }
}
