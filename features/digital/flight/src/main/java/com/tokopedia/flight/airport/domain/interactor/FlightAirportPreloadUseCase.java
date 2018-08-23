package com.tokopedia.flight.airport.domain.interactor;

import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by furqan on 09/08/18.
 */

public class FlightAirportPreloadUseCase extends UseCase<Boolean> {

    private FlightRepository flightRepository;

    @Inject
    public FlightAirportPreloadUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return flightRepository.checkPreloadAirport();
    }

    public RequestParams getEmptyParams() {
        return RequestParams.EMPTY;
    }
}
