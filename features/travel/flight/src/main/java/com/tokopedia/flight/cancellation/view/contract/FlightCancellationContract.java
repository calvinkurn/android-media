package com.tokopedia.flight.cancellation.view.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationPassengerModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightCancellationJourney;

import java.util.List;
import java.util.Map;

/**
 * @author by furqan on 21/03/18.
 */

public interface FlightCancellationContract {

    interface View extends CustomerView {

        void renderCancelableList();

        void setFlightCancellationViewModel(List<FlightCancellationModel> flightCancellationViewModelList);

        void setSelectedCancellationViewModel(List<FlightCancellationModel> flightCancellationViewModelList);

        void setPassengerRelations(Map<String, FlightCancellationPassengerModel> passengerRelations);

        String getInvoiceId();

        String getString(int resId);

        List<FlightCancellationJourney> getFlightCancellationJourney();

        List<FlightCancellationModel> getCurrentFlightCancellationViewModel();

        List<FlightCancellationModel> getSelectedCancellationViewModel();

        Map<String, FlightCancellationPassengerModel> getPassengerRelations();

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

        void checkPassenger(FlightCancellationPassengerModel passengerViewModel, int position);

        void uncheckPassenger(FlightCancellationPassengerModel passengerViewModel, int position);

        boolean isPassengerChecked(FlightCancellationPassengerModel passengerViewModel);

        boolean canGoNext();

        void init();
    }
}
