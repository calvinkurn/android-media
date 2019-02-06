package com.tokopedia.flight.cancellation.view.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationJourney;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationPassengerViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationViewModel;

import java.util.List;
import java.util.Map;

/**
 * @author by furqan on 21/03/18.
 */

public interface FlightCancellationContract {

    interface View extends CustomerView {

        void renderCancelableList();

        void setFlightCancellationViewModel(List<FlightCancellationViewModel> flightCancellationViewModelList);

        void setSelectedCancellationViewModel(List<FlightCancellationViewModel> flightCancellationViewModelList);

        void setPassengerRelations(Map<String, FlightCancellationPassengerViewModel> passengerRelations);

        String getInvoiceId();

        String getString(int resId);

        List<FlightCancellationJourney> getFlightCancellationJourney();

        List<FlightCancellationViewModel> getCurrentFlightCancellationViewModel();

        List<FlightCancellationViewModel> getSelectedCancellationViewModel();

        Map<String, FlightCancellationPassengerViewModel> getPassengerRelations();

        void navigateToReasonAndProofPage();

        void showShouldChooseAtLeastOnePassengerError();

        void hideFullLoading();

        void showFullLoading();

        void showGetListError(Throwable throwable);

        void showAutoCheckDialog();

        boolean isFirstRelationCheck();

        void disableNextButton();

        void enableNextButton();
    }

    interface Presenter {

        void onViewCreated();

        void onNextButtonClicked();

        void checkPassenger(FlightCancellationPassengerViewModel passengerViewModel, int position);

        void uncheckPassenger(FlightCancellationPassengerViewModel passengerViewModel, int position);

        boolean isPassengerChecked(FlightCancellationPassengerViewModel passengerViewModel);

        boolean canGoNext();

        void init();
    }
}
