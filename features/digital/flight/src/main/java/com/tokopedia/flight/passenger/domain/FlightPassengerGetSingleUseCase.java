package com.tokopedia.flight.passenger.domain;

import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.passenger.domain.model.ListPassengerViewModelMapper;
import com.tokopedia.flight_dbflow.FlightPassengerDB;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by furqan on 14/03/18.
 */

public class FlightPassengerGetSingleUseCase extends UseCase<FlightBookingPassengerViewModel> {

    private static final String PARAM_PASSENGER_ID = "PARAM_PASSENGER_ID";
    private static final String DEFAULT_STRING_VALUE = "";

    private final FlightRepository flightRepository;
    private final ListPassengerViewModelMapper listPassengerViewModelMapper;

    @Inject
    public FlightPassengerGetSingleUseCase(FlightRepository flightRepository,
                                           ListPassengerViewModelMapper listPassengerViewModelMapper) {
        this.flightRepository = flightRepository;
        this.listPassengerViewModelMapper = listPassengerViewModelMapper;
    }

    @Override
    public Observable<FlightBookingPassengerViewModel> createObservable(RequestParams requestParams) {
        return flightRepository.getSinglePassengerById(
                requestParams.getString(PARAM_PASSENGER_ID, DEFAULT_STRING_VALUE))
                .map(new Func1<FlightPassengerDB, FlightBookingPassengerViewModel>() {
                    @Override
                    public FlightBookingPassengerViewModel call(FlightPassengerDB flightPassengerDb) {
                        return listPassengerViewModelMapper.transform(flightPassengerDb);
                    }
                });
    }

    public RequestParams generateRequestParams(String passengerId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_PASSENGER_ID, passengerId);
        return requestParams;
    }
}
