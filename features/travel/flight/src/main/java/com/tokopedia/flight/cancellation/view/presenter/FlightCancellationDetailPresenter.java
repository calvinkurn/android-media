package com.tokopedia.flight.cancellation.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationDetailContract;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListPassengerModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListModel;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;

import java.util.List;

import javax.inject.Inject;

/**
 * @author by furqan on 03/05/18.
 */

public class FlightCancellationDetailPresenter extends BaseDaggerPresenter<FlightCancellationDetailContract.View>
        implements FlightCancellationDetailContract.Presenter {

    @Inject
    public FlightCancellationDetailPresenter() {
    }

    @Override
    public void onViewCreated() {
        addAirportDetailsToPassengers();
    }

    private void addAirportDetailsToPassengers() {
        FlightCancellationListModel cancellationListViewModelList = getView().getFlightCancellationList();

        for (FlightCancellationListPassengerModel passengerItem : cancellationListViewModelList.getCancellations().getPassengers()) {
            passengerItem.setDepartureAiportId(getDepartureAirportId(cancellationListViewModelList.getCancellations().getJourneys(), passengerItem.getJourneyId()));
            passengerItem.setArrivalAirportId(getArrivalAirportId(cancellationListViewModelList.getCancellations().getJourneys(), passengerItem.getJourneyId()));
        }
    }

    private String getDepartureAirportId(List<FlightOrderJourney> journeyList, long journeyId) {
        String airportId = "";

        for (FlightOrderJourney journeyItem : journeyList) {
            if (journeyItem.getJourneyId() == journeyId) {
                airportId = journeyItem.getDepartureAiportId();
                break;
            }
        }

        return airportId;
    }

    private String getArrivalAirportId(List<FlightOrderJourney> journeyList, long journeyId) {
        String airportId = "";

        for (FlightOrderJourney journeyItem : journeyList) {
            if (journeyItem.getJourneyId() == journeyId) {
                airportId = journeyItem.getArrivalAirportId();
                break;
            }
        }

        return airportId;
    }
}
